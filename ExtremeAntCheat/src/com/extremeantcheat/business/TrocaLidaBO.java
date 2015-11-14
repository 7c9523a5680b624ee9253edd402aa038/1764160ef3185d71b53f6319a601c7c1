package com.extremeantcheat.business;

import com.extremeantcheat.entity.SerialPlayer;

public class TrocaLidaBO {

	private SerialPlayerBO serialPlayerBO;
	
	public TrocaLidaBO() {
		serialPlayerBO = new SerialPlayerBO();
	}
	
	public SerialPlayer findBySerialHash(String serialHash) {
		return serialPlayerBO.findByHash(serialHash);
	}

}
