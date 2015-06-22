package com.domin.ui.util;

import java.io.Serializable;
import java.util.concurrent.Callable;

import com.domin.net.DominClient;

@SuppressWarnings("hiding")
public class DominCallable<Void> implements Callable<Void>, Serializable {

	private static final long serialVersionUID = 1L;	
	
	@Override
	public Void call() throws Exception {		
		return null;
	}
	
	public Void callWithConnection(DominClient connection) throws Exception {
		return null;
	}

}
