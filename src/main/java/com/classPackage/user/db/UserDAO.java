package com.classPackage.user.db;

import java.sql.SQLException;

import com.classPackage.user.logic.User;

public interface UserDAO {
	public void createTable();
	public int insertUserRecord(User User);
	public User loginCheck(String email, String password); 
	public void updateUserRecord(int id, String colunmnName, String value);
	public void deleteRecordFromUsers(int serialNum);
	public User getUserByKeyParam(String columnName, Object value) throws SQLException;
}
