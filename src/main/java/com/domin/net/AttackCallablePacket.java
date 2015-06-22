package com.domin.net;

import java.io.Serializable;

import javafx.scene.image.Image;

import com.domin.ui.util.DominCallable;

public class AttackCallablePacket implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DominCallable<Void> callable;
	private boolean isSimultaneous;
	private String imagePath;
	
	public AttackCallablePacket(DominCallable<Void> callable, String imagePath) {
		this.callable = callable;
		this.isSimultaneous = false;
		this.imagePath = imagePath;
	}
	
	public AttackCallablePacket(DominCallable<Void> callable, boolean isSimultaneous, String imagePath) {
		this.callable = callable;
		this.isSimultaneous = isSimultaneous;
		this.imagePath = imagePath;
	}	
	
	public boolean isSimultaneuous() {
		return isSimultaneous;
	}
	
	public Image getAttackCardImage() {
		return new Image(imagePath);
	}

	public Void callWithConnection(DominClient connection) {
		try {
			return callable.callWithConnection(connection);
		} catch (Exception e) {
			System.out.println("Could not call attack callable with connection");
			e.printStackTrace();
		}
		return null;
	}
	
}
