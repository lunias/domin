package com.domin.net;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class CallablePacket implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private Callable<Void> callable;
	
	public CallablePacket(Callable<Void> callable) {
		this.callable = callable;
	}
	
	public Void call() {
		try {
			return callable.call();
		} catch (Exception e) {			
			System.out.println("Could not call callable " + e.getMessage());
		}
		return null;
	}

}
