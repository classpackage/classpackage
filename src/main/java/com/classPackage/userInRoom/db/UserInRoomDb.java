package com.classPackage.userInRoom.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;

import com.classPackage.dbUtils.DBcloser;
import com.classPackage.userInRoom.logic.UserInRoom;

public class UserInRoomDb implements UserInRoomDAO {
	private Connection conn;
	private static final String USERS_IN_ROOM_TABLE = "users_in_room";
	private static final String ROOMS_TABLE = "rooms";
	private static final String USERS_TABLE = "users";

	public UserInRoomDb(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable() {
		try (Statement st = conn.createStatement();) {

			String SQL = " CREATE TABLE IF NOT EXISTS " + USERS_IN_ROOM_TABLE;
			SQL += " ( ";
			SQL += " serial_num MEDIUMINT NOT NULL AUTO_INCREMENT, ";
			SQL += " room_id MEDIUMINT NOT NULL, ";
			SQL += " user_id MEDIUMINT NOT NULL, ";
			SQL += " rate MEDIUMINT NOT NULL, ";
			SQL += " rate_time Timestamp ON UPDATE CURRENT_TIMESTAMP, ";
			SQL += " PRIMARY KEY (serial_num), ";
			SQL += " FOREIGN KEY (user_id) REFERENCES " + USERS_TABLE
					+ "(serial_num) ";
			SQL += " ON DELETE CASCADE, ";
			SQL += " FOREIGN KEY (room_id) REFERENCES " + ROOMS_TABLE
					+ "(serial_num) ";
			SQL += " ON DELETE CASCADE, ";
			SQL += " CONSTRAINT UNIQUE (room_id,user_id)";
			SQL += " ) ";
			SQL += " ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin; ";
			st.executeUpdate(SQL);
			System.out.println("Created the table ---> " + USERS_IN_ROOM_TABLE);
		} catch (SQLException ex) {
			System.err.println("error in creating table: "
					+ USERS_IN_ROOM_TABLE + " :: " + ex.getMessage());
		}
	}

	@Override
	public int insertUserInRoomRecord(UserInRoom UserInRoom) {
		reOpenDBConn();
		String SQL = "INSERT INTO " + USERS_IN_ROOM_TABLE
				+ " (user_id, room_id, rate, rate_time)" + " VALUES (?,?,?,?);";
		System.out.println("insert SQL=" + SQL);
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, UserInRoom.getUserId());
			preparedStatement.setInt(2, UserInRoom.getRoomId());
			preparedStatement.setInt(3, UserInRoom.getRate());
			preparedStatement.setNull(4, Types.DATE);
			preparedStatement.executeUpdate();
			preparedStatement.close();

			System.out.println("Created Record to " + USERS_IN_ROOM_TABLE);
		}

		catch (SQLException ex) {
			System.err.println("error with inserting record to " + USERS_TABLE
					+ ": " + ex.getMessage());
		}

		return UserInRoom.getUserId();
	}

	@Override
	public void updateUserInRoomRate(int userId, int roomId, int rate) {
		reOpenDBConn();
		String SQL = "UPDATE " + USERS_IN_ROOM_TABLE
				+ " SET rate = ? WHERE user_id  = " + userId
				+ " AND room_id = " + roomId;

		System.out.println("insert SQL=" + SQL);
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, rate);
			int update = preparedStatement.executeUpdate();
			if (update == 1) {
				System.out.println("Row is updated.");
			} else {
				System.out.println("Row is not updated");
			}

			System.out.println("update Record in " + USERS_IN_ROOM_TABLE
					+ "user id = " + userId + " and room id = " + roomId);
		}

		catch (SQLException ex) {
			System.err.println("error with updating record in "
					+ USERS_IN_ROOM_TABLE + ": " + ex.getMessage());
		}
	}

	@Override
	public void deleteUserInRoom(int userId, int roomId) {
		int ownerId = getRoomOwnerId(roomId);
		try {
			if (userId == ownerId) {
				deleteAllUsersInRoom(roomId);
			} else {
				deleteSingleUserInRoom(userId, roomId);
			}
		} catch (SQLException ex) {
			System.err.println("SQL statement: deleteing user with"
					+ "user_id = " + userId + " and room_id " + roomId
					+ "is not executed! : " + ex.getMessage());
		}
	}

	@Override
	public List<UserInRoom> getAllUsersInRoom(int roomId) {
		List<UserInRoom> userInRoomList = new ArrayList<UserInRoom>();
		reOpenDBConn();
		String SQL = "SELECT distinct " + USERS_IN_ROOM_TABLE + ".* FROM "
				+ USERS_IN_ROOM_TABLE + " WHERE room_id = ?";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					userInRoomList.add(mapUsersToList(resultSet));
					System.out
							.println("result of query is sellers list in size of : "
									+ userInRoomList.size());
				}

			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

		System.out
				.println("SQL statement selecting all users in room was exscuted.");
		return userInRoomList;
	}

	@Override
	public double getRateAvg(int roomId) {
		double avg = 0;

		reOpenDBConn();
		String SQL = " SELECT AVG (rate) as avg " + " FROM "
				+ USERS_IN_ROOM_TABLE + " WHERE room_id = ?";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					avg = resultSet.getDouble("avg");
				}
			}

		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

		System.out
				.println("SQL statement selecting rate average of all users in room was exscuted.");
		return avg;
	}

	/*
	 * Creates a userInRoom object from ResultSet that will be inserted to
	 * 'userInRoom' list -WORKS returns UserInRoom
	 */

	private UserInRoom mapUsersToList(ResultSet rs) throws SQLException {
		UserInRoom UserInRoom;
		try {
			int roomId = rs.getInt("room_id");
			int userId = rs.getInt("user_id");
			UserInRoom = new UserInRoom(roomId, userId);
			UserInRoom.setRate(rs.getInt("rate"));
			UserInRoom.setRateTime(rs.getTimestamp("rate_time"));
		} catch (SQLException ex) {
			throw (ex);
		}

		return UserInRoom;
	}

	private int getRoomOwnerId(int roomId) {
		int ownerId = -1;

		reOpenDBConn();
		String SQL = "SELECT owner_id FROM " + ROOMS_TABLE
				+ " WHERE serial_num = ?";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					ownerId = resultSet.getInt("owner_id");
					System.out.println("room number : " + roomId
							+ "owner id is: " + ownerId);
				}
			}

		} catch (SQLException ex) {
			System.err.println("error in getting user's owner id. "
					+ ex.getMessage());
		}

		return ownerId;
	}

	private int countStudentsRate(int minVal, int maxVal, int roomId) {
		int count = 0;

		reOpenDBConn();
		ResultSet resultSet = null;
		String SQL = " SELECT COUNT (rate) as count " + " FROM "
				+ USERS_IN_ROOM_TABLE
				+ " WHERE rate between ? and ? AND room_id = ? ";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt("count");
				System.out.println("room number : " + roomId + "rate between "
						+ minVal + " - " + maxVal + " is: " + count);
			}

		} catch (SQLException ex) {
			System.err.println("error in getting rate count. "
					+ ex.getMessage());
		}

		return count;
	}

	private void deleteSingleUserInRoom(int userId, int roomId)
			throws SQLException {
		reOpenDBConn();
		String SQL = "DELETE FROM " + USERS_IN_ROOM_TABLE
				+ " WHERE user_id = ? AND room_id = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, roomId);
			System.out.println("DELETE action, SQL=" + SQL);
			int delete = preparedStatement.executeUpdate();
			if (delete == 1) {
				System.out.println("Row:user id " + userId + " in room id"
						+ roomId + "is deleted.");
			} else {
				System.out.println("Row:user id " + userId + " in room id"
						+ roomId + "is not deleted.");
			}
		} catch (SQLException ex) {
			throw (ex);
		}
	}

	private void deleteAllUsersInRoom(int roomId) throws SQLException {
		reOpenDBConn();

		String SQL = "DELETE FROM " + USERS_IN_ROOM_TABLE
				+ " WHERE room_id = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			System.out.println("DELETE action, SQL=" + SQL);
			int delete = preparedStatement.executeUpdate();
			if (delete == 1) {
				System.out.println("All users in room id " + roomId
						+ "were deleted.");
			} else {
				System.out.println("not!!all users in room id " + roomId
						+ "were deleted.");
			}
		} catch (SQLException ex) {
			throw (ex);
		}
	}

	private void reOpenDBConn() {
		this.conn = DBcloser.reOpenConnDB(conn);
	}
}
