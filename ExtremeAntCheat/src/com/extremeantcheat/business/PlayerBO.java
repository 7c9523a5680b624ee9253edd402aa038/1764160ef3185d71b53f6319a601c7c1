package com.extremeantcheat.business;

import java.util.List;

import com.extremeantcheat.dao.PlayerDAO;
import com.extremeantcheat.entity.Player;

public class PlayerBO {

	private PlayerDAO playerDAO;
	
	public PlayerBO() {
		playerDAO = new PlayerDAO();
	}
	
	public List<Player> findAll(int serialPlayerId) {
		return playerDAO.findAllPlayerOn(serialPlayerId);
	}

}