package com.domin.net.common;

import java.io.Serializable;

final class DisconnectMessage implements Serializable {

    private static final long serialVersionUID = 6980380891606133963L;

	final public String message;

	public DisconnectMessage(String message) {
		this.message = message;
	}

}
