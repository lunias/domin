package com.domin.net.common;

import java.io.Serializable;

public class ChatPacket implements Serializable {

	private static final long serialVersionUID = 736516397212000148L;
	private String message;
	private String username;

	public ChatPacket(String message, String username) {
		this.message = message;
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public String getUsername() {
		return username;
	}

	public String toString() {
		return message;
	}

}
