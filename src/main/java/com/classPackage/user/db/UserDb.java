package com.classPackage.user.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.classPackage.dbUtils.DBcloser;
import com.classPackage.user.logic.User;
import com.classPackage.user.db.UserDAO;
import com.classPackage.utils.DateTimeUtils;

public class UserDb implements UserDAO {

	private Connection conn;
	private static final String USERS_TABLE = "users";

	public UserDb(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable() {
		try (Statement st = conn.createStatement();) {

			String SQL = " CREATE TABLE IF NOT EXISTS " + USERS_TABLE;
			SQL += " ( ";
			SQL += " serial_num MEDIUMINT NOT NULL AUTO_INCREMENT, ";
			SQL += " first_name VARCHAR(15) NOT NULL, ";
			SQL += " last_name VARCHAR(15) NOT NULL, ";
			SQL += " password VARCHAR(100) NOT NULL, ";
			SQL += " email VARCHAR(200) NOT NULL, ";
			SQL += " last_Login TIMESTAMP, ";
			SQL += " is_lecuterer BOOLEAN NOT NULL, ";
			SQL += " CONSTRAINT user_unique UNIQUE (email), ";
			SQL += " PRIMARY KEY (serial_num)";
			SQL += " ) ";
			SQL += " ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin; ";
			st.executeUpdate(SQL);
			System.out.println("Created the table ---> " + USERS_TABLE);
		} catch (SQLException ex) {
			System.err.println("error in creating table: " + USERS_TABLE
					+ " :: " + ex.getMessage());
		}
	}

	@Override
	public int insertUserRecord(User user) {
		int userSerialNum = 0;

		reOpenDBConn();
		String SQL = "INSERT INTO "
				+ USERS_TABLE
				+ " (first_name, last_name, password, email, last_login, is_lecuterer)"
				+ " VALUES (?,?,?,?,?,?);";
		System.out.println("insert SQL=" + SQL);
		if (getUserSerialNum(user.getEmail()) < 0) {
			try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
				preparedStatement.setString(1, user.getFirstName());
				preparedStatement.setString(2, user.getLastName());
				preparedStatement.setString(3,
						BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
				preparedStatement.setString(4, user.getEmail());
				preparedStatement.setTimestamp(5,
						DateTimeUtils.getCurrentTimeStamp(user.getLastLogin()));
				preparedStatement.setBoolean(6, user.getIsLecturer());

				preparedStatement.executeUpdate();
				System.out.println("Created Record to " + USERS_TABLE);
				userSerialNum = getUserSerialNum(user.getEmail());
				System.out.println("user was added to: " + USERS_TABLE
						+ " table, with serial number: " + userSerialNum + ".");
			} catch (SQLException | ParseException ex) {
				System.err.println("error with inserting record to "
						+ USERS_TABLE + ": " + ex.getMessage());
			}
		}

		return userSerialNum;
	}

	@Override
	public User loginCheck(String email, String password) {
		User user = null;
		if (getUserSerialNum(email) != -1) {
			user = getUserByKeyParam("email", email);
			if (user != null) {
				System.out.println("login user: " + user.getFirstName()
						+ " " + user.getLastName());
				if (BCrypt.checkpw(password, user.getPassword()) == true) {
					updateUserRecord(user.getSerialNum(), "last_login",
							user.getLastLogin());
				}
				else {
					throw new IllegalArgumentException("The password is not correct");
				}
			}
		}

		return user;
	}

	@Override
	public void deleteRecordFromUsers(int serialNum) {
		reOpenDBConn();
		String SQL = "DELETE FROM " + USERS_TABLE + " WHERE serial_num = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			System.out.println("DELETE action, SQL=" + SQL);
			preparedStatement.setInt(1, serialNum);
			int delete = preparedStatement.executeUpdate(SQL);
			if (delete == 1) {
				System.out.println("Row:id " + serialNum + " is deleted.");
			} else {
				System.out.println("Row:id " + serialNum + "is not deleted.");
			}
		} catch (SQLException ex) {
			System.err.println("SQL statement: deleteing user with"
					+ "serial_num=" + serialNum + "is not executed! : "
					+ ex.getMessage());
		}
	}

	@Override
	public void updateUserRecord(int id, String colunmnName, String value) {
		reOpenDBConn();
		String SQL = "UPDATE " + USERS_TABLE + " SET " + colunmnName
				+ "= ?  WHERE serial_num = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			if (colunmnName.equals("password")) {
				preparedStatement.setString(1,
						BCrypt.hashpw(value, BCrypt.gensalt()));
			} else if (colunmnName.equals("last_login")) {
				preparedStatement.setTimestamp(1,
						DateTimeUtils.getCurrentTimeStamp(value));
			} else if (colunmnName.equals("is_lecuterer")) {
				preparedStatement.setBoolean(1, Boolean.valueOf(value));
			} else /* if(colunmnName.equals("email")) */{
				preparedStatement.setString(1, value);
			}
			preparedStatement.setInt(2, id);

			System.out.println("UPDATE action, SQL=" + SQL);
			int update = preparedStatement.executeUpdate();
			if (update == 1) {
				System.out.println("Row:id " + id + " is updated.");
			} else {
				System.out.println("Row:id " + id + " is not updated.");
			}
		} catch (SQLException | ParseException ex) {
			System.err.println("SQL statement: " + SQL
					+ " was not executed! :  " + ex.getMessage());
		}
	}

	/*
	 * helper function of insertToUser- get user's serial_num using email as
	 * unique key returns serial num
	 */
	private int getUserSerialNum(String email) {
		int serialNum = -1;

		String SQL = "SELECT serial_num FROM " + USERS_TABLE
				+ " WHERE email = ?";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs != null && rs.next()) {
				serialNum = rs.getInt("serial_num");
			}

		} catch (SQLException ex) {
			System.err.println("error in getting user's serial nummber. "
					+ ex.getMessage());
		}

		return serialNum;
	}

	@Override
	public User getUserByKeyParam(String columnName, Object value) {
		User user = null;

		reOpenDBConn();
		String SQL = "SELECT * FROM " + USERS_TABLE + " WHERE " + columnName
				+ " = ?";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			if (columnName.equals("email")) {
				preparedStatement.setString(1, value.toString());
			} else {
				preparedStatement.setInt(1, Integer.valueOf(value.toString()));
			}
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					user = mapUsersToList(resultSet);
					System.out.println(" execute query " + SQL);
				}
			}
		} catch (SQLException ex) {
			System.err.println("SQL statement: " + SQL
					+ " was not executed! :  " + ex.getMessage());
		}

		return user;
	}

	private User mapUsersToList(ResultSet resultSet) throws SQLException {
		User user;
		try {
			int serialNum = resultSet.getInt("serial_num");
			String firstName = resultSet.getString("first_name");
			String lastName = resultSet.getString("last_Name");
			String password = resultSet.getString("password");
			String email = resultSet.getString("email");
			boolean isLecturer = resultSet.getBoolean("is_lecuterer");
			user = new User(serialNum, firstName, lastName, password, email,
					isLecturer);
		} catch (SQLException ex) {
			throw (ex);
		}

		return user;
	}

	public boolean checkEqualPassword(int serialNum, String oldPassword) {
		boolean res = false;
		User user = null;
		user = getUserByKeyParam("serial_num", serialNum);
		if (BCrypt.checkpw(oldPassword, user.getPassword()) == true) {
			res = true;
		}
		
		return res;
	}

	private void reOpenDBConn() {
		this.conn = DBcloser.reOpenConnDB(conn);
	}
	
}
