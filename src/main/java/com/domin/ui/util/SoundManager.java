package com.domin.ui.util;

import javafx.scene.media.AudioClip;

public enum SoundManager {

	ACTIVE("active.mp3"), ALERT("alert.mp3"), COIN("coin.mp3"), SHUFFLE("shuffle.mp3");
	
	private final String filePath;
	private final AudioClip audioClip;	
	
	private SoundManager(String fileName) {		
		
		this.filePath = SoundManager.class.getResource("/snd/" + fileName).toString();
		
		this.audioClip = new AudioClip(filePath);		
		
	}
	
	public void play() {
		audioClip.play(StageManager.INSTANCE.getVolumeProp().get());
	}	
	
	public String toString() {
		return filePath;
	}
	
}
