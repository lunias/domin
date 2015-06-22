package com.domin.net;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class VictoryDisplayPacket implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<VictoryPacket> victoryPacketList;
	
	public VictoryDisplayPacket(List<VictoryPacket> victoryPacketList) {
		this.victoryPacketList = victoryPacketList;
	}
	
	public List<VictoryPacket> getVictoryPacketList() {
		return victoryPacketList;
	}
	
}
