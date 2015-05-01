package com.classPackage.poll.logic;

public class poll {
	int roomId;
	int ordering;
	
	/**
	 * @param roomId
	 * @param ordering
	 */
	public poll(int roomId, int ordering) {
		super();
		this.roomId = roomId;
		this.ordering = ordering;
	}
	/**
	 * @return the roomId
	 */
	public int getRoomId() {
		return roomId;
	}
	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	/**
	 * @return the ordering
	 */
	public int getOrdering() {
		return ordering;
	}
	/**
	 * @param ordering the ordering to set
	 */
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	
}
