package com.domin.net;

import java.io.Serializable;

public class HandshakePacket implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private double version;
	
	public HandshakePacket(String username, double version) {
		
		this.username = username;
		this.version = version;
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public double getVersion() {
		return version;
	}
	
}
