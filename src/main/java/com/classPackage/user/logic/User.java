package com.classPackage.user.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.classPackage.utils.DateTimeUtils;

public class User {
	
	final String firstName;
	final String lastName;
	private int serialNum;
	private String password;
	private String email;
	private String lastLogin;
	private boolean isLecturer;
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param email
	 */
	
	public User(String firstName, String lastName, String password, String email, boolean isLecturer) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.isLecturer = isLecturer;
		setLastLogin();
	}
	
	/**
	 * @param serialNum
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param email
	 */
	
	public User(int serialNum, String firstName, String lastName, String password, String email, boolean isLecturer) {
		this.serialNum = serialNum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.isLecturer = isLecturer;
		setLastLogin();
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**

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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setLastLogin() {
		DateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.formater);
		Date date = new Date();
		this.lastLogin = dateFormat.format(date);
	}
	
	public String getLastLogin() {
		return this.lastLogin;
	}

	/**
	 * @return the serialNum
	 */
	public int getSerialNum() {
		return serialNum;
	}

	/**
	 * @param serialNum the serialNum to set
	 */
	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}

	/**
	 * @return the isLecturer
	 */
	public boolean getIsLecturer() {
		return isLecturer;
	}

	/**
	 * @param isLecturer the isLecturer to set
	 */
	public void setIsLecturer(boolean isLecturer) {
		this.isLecturer = isLecturer;
	}
}
