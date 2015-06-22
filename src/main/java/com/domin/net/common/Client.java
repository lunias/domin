package com.domin.net.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

abstract public class Client {

	protected int[] connectedPlayerIDs = new int[0];
	protected String username;

	private volatile boolean autoreset;

	public Client(String hubHostName, int hubPort, String username)
			throws IOException {
		this.username = username;
		connection = new ConnectionToHub(hubHostName, hubPort);
	}

	abstract protected void messageReceived(Object message);

	protected void playerConnected(int newPlayerID) {
	}

	protected void playerDisconnected(int departingPlayerID) {
	}

	protected void connectionClosedByError(String message) {
	}

	protected void serverShutdown(String message) {
	}

	protected void extraHandshake(ObjectInputStream in, ObjectOutputStream out)
			throws IOException {
	}

	public void disconnect() {
		if (!connection.closed)
			connection.send(new DisconnectMessage("Goodbye Hub"));
	}

	public void send(Object message) {
		if (message == null)
			throw new IllegalArgumentException(
					"Null cannot be sent as a message.");
		if (!(message instanceof Serializable))
			throw new IllegalArgumentException(
					"Messages must implement the Serializable interface.");
		if (connection.closed)
			throw new IllegalStateException(
					"Message cannot be sent because the connection is closed.");
		connection.send(message);
	}

	public int getID() {
		return connection.id_number;
	}

	public void resetOutput() {
		connection.send(new ResetSignal());
	}

	public void setAutoreset(boolean auto) {
		autoreset = auto;
	}

	public boolean getAutoreset() {
		return autoreset;
	}

	private final ConnectionToHub connection;

	private class ConnectionToHub {

		private final int id_number;
		private final Socket socket;
		private final ObjectInputStream in;
		private final ObjectOutputStream out;
		private final SendThread sendThread;
		private final ReceiveThread receiveThread;

		private final LinkedBlockingQueue<Object> outgoingMessages;

		private volatile boolean closed;

		ConnectionToHub(String host, int port) throws IOException {
			outgoingMessages = new LinkedBlockingQueue<Object>();
			socket = new Socket(host, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject("Hello Hub");
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			try {
				Object response = in.readObject();
				id_number = ((Integer) response).intValue();
			} catch (Exception e) {
				throw new IOException("Illegal response from server.");
			}
			extraHandshake(in, out);
			sendThread = new SendThread();
			receiveThread = new ReceiveThread();
			sendThread.start();
			receiveThread.start();
		}

		void close() {
			closed = true;
			sendThread.interrupt();
			receiveThread.interrupt();
			try {
				socket.close();
			} catch (IOException e) {
			}
		}

		void send(Object message) {
			outgoingMessages.add(message);
		}

		synchronized void closedByError(String message) {
			if (!closed) {
				connectionClosedByError(message);
				close();
			}
		}

		private class SendThread extends Thread {
			public void run() {
				System.out.println("Client send thread started.");
				try {
					while (!closed) {
						Object message = outgoingMessages.take();
						if (message instanceof ResetSignal) {
							out.reset();
						} else {
							if (autoreset)
								out.reset();
							out.writeObject(message);
							out.flush();
							if (message instanceof DisconnectMessage) {
								close();
							}
						}
					}
				} catch (IOException e) {
					if (!closed) {
						closedByError("IO error occurred while trying to send message.");
						System.out
								.println("Client send thread terminated by IOException: "
										+ e);
					}
				} catch (Exception e) {
					if (!closed) {
						closedByError("Unexpected internal error in send thread: "
								+ e);
						System.out
								.println("\nUnexpected error shuts down client send thread:");
						e.printStackTrace();
					}
				} finally {
					System.out.println("Client send thread terminated.");
				}
			}
		}

		private class ReceiveThread extends Thread {
			public void run() {
				System.out.println("Client receive thread started.");
				try {
					while (!closed) {
						Object obj = in.readObject();
						if (obj instanceof DisconnectMessage) {
							close();
							serverShutdown(((DisconnectMessage) obj).message);
						} else if (obj instanceof StatusMessage) {
							StatusMessage msg = (StatusMessage) obj;
							connectedPlayerIDs = msg.players;
							if (msg.connecting)
								playerConnected(msg.playerID);
							else
								playerDisconnected(msg.playerID);
						} else
							messageReceived(obj);
					}
				} catch (IOException e) {
					if (!closed) {
						closedByError("IO error occurred while waiting to receive  message.");
						System.out
								.println("Client receive thread terminated by IOException: "
										+ e);
					}
				} catch (Exception e) {
					if (!closed) {
						closedByError("Unexpected internal error in receive thread: "
								+ e);
						System.out
								.println("\nUnexpected error shuts down client receive thread:");
						e.printStackTrace();
					}
				} finally {
					System.out.println("Client receive thread terminated.");
				}
			}
		}

	}

}
