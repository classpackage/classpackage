package com.classPackage.userInRoom.logic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class UserInRoom {
	
	final int roomId;
	final int userId;
	int rate;
	String rateTime;
	
	/**
	 * @param roomId
	 * @param userId
	 * @param rate
	 * @param rateTime
	 */
	public UserInRoom(int roomId, int userId) {
		this.roomId = roomId;
		this.userId = userId;
	}
	/**
	 * @return the rate
	 */
	public int getRate() {
		return rate;
	}
	/**
	 * @param rate the rate to set
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}
	/**
	 * @return the rateTime
	 */
	public String getRateTime() {
		return rateTime;
	}
	/**
	 * @param rateTime the rateTime to set
	 */
	public void setRateTime(Timestamp rateTime) {
		this.rateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rateTime);
	}
	
	/**
	 * @return the roomId
	 */
	public int getRoomId() {
		return roomId;
	}
	
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
}

