package com.classPackage.room.logic;

import com.classPackage.utils.DateTimeUtils;
import java.util.Date;

public class Room {
	
	private int roomNumber;
	private int ownerId;
	private String roomName;
	private String password;
	private Date startDate;
	private Date endDate;
	private boolean isPolls;
	private boolean isQuestions;
	private boolean isAutorized;
	
	/**
	 * @param roomId
	 * @param ownerId
	 * @param password
	 * @param startDate
	 * @param endDate
	 **/
	public Room(int ownerId, String password, String roomName, String startDate,
			String endDate, boolean isPolls, boolean isQuestions) {
		this.ownerId = ownerId;
		this.password = password;
		this.roomName = roomName;
		this.isPolls = isPolls;
		this.isQuestions =isQuestions;
		this.startDate = DateTimeUtils.formatStringToDate(startDate);
		this.endDate = DateTimeUtils.formatStringToDate(endDate);
		this.isAutorized = false;
		if(this.endDate.before(this.startDate)) {
			throw new IllegalArgumentException("room dates are not compatibale");
		}
	}
	
	public Room(int roomNumber, int ownerId, String password, String roomName, Date startDate,
			Date endDate, boolean isPolls, boolean isQuestions, boolean isAutorized) {
		this.roomNumber = roomNumber;
		this.ownerId = ownerId;
		this.password = password;
		this.roomName = roomName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isQuestions = isQuestions;
		this.isPolls = isPolls;
		this.isAutorized = isAutorized;
	}
	
	/**
	 * @return the ownerId
	 */
	public int getOwnerId() {
		return ownerId;
	}
	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the roomNumber
	 */
	public int getRoomNumber() {
		return this.roomNumber;
	}
	/**
	 * @param roomNumber the roomNumber to set
	 */
	public void setRooomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	/**
	 * @return the roomName
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * @param roomName the roomName to set
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/**
	 * @return the isPolls
	 */
	public boolean getIsPolls() {
		return isPolls;
	}

	/**
	 * @param isPolls the isPolls to set
	 */
	public void setPolls(boolean isPolls) {
		this.isPolls = isPolls;
	}

	/**
	 * @return the isQuestions
	 */
	public boolean getIsQuestions() {
		return isQuestions;
	}

	/**
	 * @param isQuestions the isQuestions to set
	 */
	public void setQuestions(boolean isQuestions) {
		this.isQuestions = isQuestions;
	}

	/**
	 * @return the isAutorized
	 */
	public boolean isAutorized() {
		return isAutorized;
	}

	/**
	 * @param isAutorized the isAutorized to set
	 */
	public void setAutorized(boolean isAutorized) {
		this.isAutorized = isAutorized;
	}

	/**
	 * @param roomNumber the roomNumber to set
	 */
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
}
