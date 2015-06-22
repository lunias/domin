package com.domin.net.common;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Hub {

    private TreeMap<Integer, ConnectionToClient> playerConnections;

    private LinkedBlockingQueue<Message> incomingMessages;

    private volatile boolean autoreset;

    private ServerSocket serverSocket;
    private Thread serverThread;
    volatile private boolean shutdown;

    private int nextClientID = 0;

    public Hub(int port) throws IOException {
        playerConnections = new TreeMap<Integer, ConnectionToClient>();
        incomingMessages = new LinkedBlockingQueue<Message>();
        serverSocket = new ServerSocket(port);
        System.out.println("Listening for client connections on port " + port);
        serverThread = new ServerThread();
        serverThread.setDaemon(true);
        serverThread.start();
        Thread readerThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Message msg = incomingMessages.take();
                        messageReceived(msg.playerConnection, msg.message);
                    } catch (Exception e) {
                        System.out
                                .println("Exception while handling received message:");
                        e.printStackTrace();
                    }
                }
            }
        };
        readerThread.setDaemon(true);
        readerThread.start();
    }

    protected void messageReceived(int playerID, Object message) {
        sendToAll(new ForwardedMessage(playerID, message));
    }

    protected void playerConnected(int playerID) {
    }

    protected void playerDisconnected(int playerID) {
    }

    protected boolean extraHandshake(int playerID, ObjectInputStream in,
            ObjectOutputStream out) throws IOException {
    	return true;
    }

    synchronized public int[] getPlayerList() {
        int[] players = new int[playerConnections.size()];
        int i = 0;
        for (int p : playerConnections.keySet())
            players[i++] = p;
        return players;
    }

    public void shutdownServerSocket() {
        if (serverThread == null)
            return;
        incomingMessages.clear();
        shutdown = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
        }
        serverThread = null;
        serverSocket = null;
    }

    public void restartServer(int port) throws IOException {
        if (serverThread != null && serverThread.isAlive())
            throw new IllegalStateException(
                    "Server is already listening for connections.");
        shutdown = false;
        serverSocket = new ServerSocket(port);
        serverThread = new ServerThread();
        serverThread.start();
    }

    public void shutDownHub() {
        shutdownServerSocket();
        sendToAll(new DisconnectMessage("*shutdown*"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        for (ConnectionToClient pc : playerConnections.values())
            pc.close();
    }

    synchronized public void sendToAll(Object message) {
        if (message == null)
            throw new IllegalArgumentException(
                    "Null cannot be sent as a message.");
        if (!(message instanceof Serializable))
            throw new IllegalArgumentException(
                    "Messages must implement the Serializable interface.");
        for (ConnectionToClient pc : playerConnections.values())
            pc.send(message);
    }

    synchronized public boolean sendToOne(int recipientID, Object message) {
        if (message == null)
            throw new IllegalArgumentException(
                    "Null cannot be sent as a message.");
        if (!(message instanceof Serializable))
            throw new IllegalArgumentException(
                    "Messages must implement the Serializable interface.");
        ConnectionToClient pc = playerConnections.get(recipientID);
        if (pc == null)
            return false;
        else {
            pc.send(message);
            return true;
        }
    }

    public void resetOutput() {
        ResetSignal rs = new ResetSignal();
        for (ConnectionToClient pc : playerConnections.values())
            pc.send(rs);
    }

    public void setAutoreset(boolean auto) {
        autoreset = auto;
    }

    public boolean getAutoreset() {
        return autoreset;
    }

    synchronized private void messageReceived(
            ConnectionToClient fromConnection, Object message) {
        int sender = fromConnection.getPlayer();
        messageReceived(sender, message);
    }

    synchronized private void acceptConnection(ConnectionToClient newConnection) {
        int ID = newConnection.getPlayer();
        playerConnections.put(ID, newConnection);
        StatusMessage sm = new StatusMessage(ID, true, getPlayerList());
        sendToAll(sm);
        playerConnected(ID);
        System.out.println("Connection accepted from client number " + ID);
    }
    
    synchronized private void denyConnection(ConnectionToClient newConnection) {
    	int ID = newConnection.getPlayer();
    	playerConnections.put(ID, newConnection);
    	clientDisconnected(ID);
    	System.out.println("Connection denied due to version mismatch.");
    }

    synchronized private void clientDisconnected(int playerID) {
        if (playerConnections.containsKey(playerID)) {
            playerConnections.remove(playerID);
            StatusMessage sm = new StatusMessage(playerID, false,
                    getPlayerList());
            sendToAll(sm);
            playerDisconnected(playerID);
            System.out.println("Connection with client number " + playerID
                    + " closed by DisconnectMessage from client.");
        }
    }

    synchronized private void connectionToClientClosedWithError(
            ConnectionToClient playerConnection, String message) {
        int ID = playerConnection.getPlayer();
        if (playerConnections.remove(ID) != null) {
            StatusMessage sm = new StatusMessage(ID, false, getPlayerList());
            sendToAll(sm);
        }
    }

    private class Message {
        ConnectionToClient playerConnection;
        Object message;
    }

    private class ServerThread extends Thread { 
        public void run() {
            try {
                while (!shutdown) {
                    Socket connection = serverSocket.accept();
                    if (shutdown) {
                        System.out.println("Listener socket has shut down.");
                        break;
                    }
                    new ConnectionToClient(incomingMessages, connection);
                }
            } catch (Exception e) {
                if (shutdown)
                    System.out.println("Listener socket has shut down.");
                else
                    System.out
                            .println("Listener socket has been shut down by error: "
                                    + e);
            }
        }
    }

    private class ConnectionToClient { 

        private int playerID; 
        private BlockingQueue<Message> incomingMessages;
        private LinkedBlockingQueue<Object> outgoingMessages;
        private Socket connection;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private volatile boolean closed;                                          
        private Thread sendThread;                                  
        private volatile Thread receiveThread;

        ConnectionToClient(BlockingQueue<Message> receivedMessageQueue,
                Socket connection) {
            this.connection = connection;
            incomingMessages = receivedMessageQueue;
            outgoingMessages = new LinkedBlockingQueue<Object>();
            sendThread = new SendThread();
            sendThread.start();
        }

        int getPlayer() {
            return playerID;
        }

        void close() {
            closed = true;
            sendThread.interrupt();
            if (receiveThread != null)
                receiveThread.interrupt();
            try {
                connection.close();
            } catch (IOException e) {
            }
        }

        void send(Object obj) { 
            if (obj instanceof DisconnectMessage) {
                outgoingMessages.clear();
            }
            outgoingMessages.add(obj);
        }

        private void closedWithError(String message) {
            connectionToClientClosedWithError(this, message);
            close();
        }

        private class SendThread extends Thread {
            public void run() {
                try {
                    out = new ObjectOutputStream(connection.getOutputStream());
                    in = new ObjectInputStream(connection.getInputStream());
                    String handle = (String) in.readObject();

                    if (!"Hello Hub".equals(handle))
                        throw new Exception(
                                "Incorrect hello string received from client.");
                    synchronized (Hub.this) {
                        playerID = nextClientID++;
                    }
                    out.writeObject(playerID);
                    out.flush();
                    if (!extraHandshake(playerID, in, out)) {
                    	denyConnection(ConnectionToClient.this);
                    	try {
                    		closed = true;
                    		connection.close();	
                    	} catch (IOException ioe) { }                    	
                    	return;
                    }
                    acceptConnection(ConnectionToClient.this);
                    receiveThread = new ReceiveThread();
                    receiveThread.start();
                } catch (Exception e) {
                    try {
                        closed = true;
                        connection.close();
                    } catch (Exception e1) {
                    }
                    System.out.println("\nError while setting up connection: "
                            + e);
                    e.printStackTrace();
                    return;
                }
                try {
                    while (!closed) {
                        try {
                            Object message = outgoingMessages.take();
                            if (message instanceof ResetSignal)
                                out.reset();
                            else {
                                if (autoreset)
                                    out.reset();
                                out.writeObject(message);
                                out.flush();
                                if (message instanceof DisconnectMessage)
                                    close();
                            }
                        } catch (InterruptedException e) {

                        }
                    }
                } catch (IOException e) {
                    if (!closed) {
                        closedWithError("Error while sending data to client.");
                        System.out
                                .println("Hub send thread terminated by IOException: "
                                        + e);
                    }
                } catch (Exception e) {
                    if (!closed) {
                        closedWithError("Internal Error: Unexpected exception in output thread: "
                                + e);
                        System.out
                                .println("\nUnexpected error shuts down hub's send thread:");
                        e.printStackTrace();
                    }
                }
            }
        }

        private class ReceiveThread extends Thread {
            public void run() {
                try {
                    while (!closed) {
                        try {
                            Object message = in.readObject();
                            Message msg = new Message();
                            msg.playerConnection = ConnectionToClient.this;
                            msg.message = message;
                            if (!(message instanceof DisconnectMessage))
                                incomingMessages.put(msg);
                            else {
                                closed = true;
                                outgoingMessages.clear();
                                out.writeObject("*goodbye*");
                                out.flush();
                                clientDisconnected(playerID);
                                close();
                            }
                        } catch (InterruptedException e) {

                        }
                    }
                } catch (IOException e) {
                    if (!closed) {
                        closedWithError("Error while reading data from client.");
                        System.out
                                .println("Hub receive thread terminated by IOException: "
                                        + e);
                    }
                } catch (Exception e) {
                    if (!closed) {
                        closedWithError("Internal Error: Unexpected exception in input thread: "
                                + e);
                        System.out
                                .println("\nUnexpected error shuts down hub's receive thread:");
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}