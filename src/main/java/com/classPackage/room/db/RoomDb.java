package com.classPackage.room.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.classPackage.dbUtils.DBcloser;
import com.classPackage.room.logic.Room;
import com.classPackage.utils.DateTimeUtils;

public class RoomDb implements RoomDAO {

	private Connection conn;
	private static final String ROOMS_TABLE = "rooms";
	private static final String USERS_TABLE = "users";

	public RoomDb(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable() {
		try (Statement st = conn.createStatement();) {
			String SQL = " CREATE TABLE IF NOT EXISTS " + ROOMS_TABLE;
			SQL += " ( ";
			SQL += " serial_num MEDIUMINT NOT NULL AUTO_INCREMENT, ";
			SQL += " owner_id MEDIUMINT NOT NULL, ";
			SQL += " room_name VARCHAR(100) NOT NULL, ";
			SQL += " password VARCHAR(100) NOT NULL, ";
			SQL += " start_date TIMESTAMP NOT NULL, ";
			SQL += " end_date TIMESTAMP NOT NULL, ";
			SQL += "isPolls BOOLEAN, ";
			SQL += "isQuestions BOOLEAN, ";
			SQL += "isAutorized BOOLEAN, ";
			SQL += " PRIMARY KEY (serial_num), ";
			SQL += " FOREIGN KEY (owner_id) REFERENCES " + USERS_TABLE
					+ "(serial_num)";
			SQL += " ON DELETE CASCADE";
			SQL += " ) ";
			SQL += " ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin; ";
			st.executeUpdate(SQL);
			System.out.println("Created the table ---> " + ROOMS_TABLE);

			alterSerialNumbers();
			System.out
					.println("generate sequence for room serial num starting from 1000 ---> "
							+ ROOMS_TABLE);

			initScheduledExecuter();
		} catch (SQLException ex) {
			System.err.println("error in creating table: " + ROOMS_TABLE
					+ " :: " + ex.getMessage());
		}
		System.out.println("Created the " + ROOMS_TABLE + " succesfully");
	}

	@Override
	public int insertRoomRecord(Room room) {
		reOpenDBConn();

		int id = -1;
		String SQL = "INSERT INTO "
				+ ROOMS_TABLE
				+ " ( owner_id, room_name, password, start_date, end_date, isPolls, isQuestions, isAutorized)"
				+ " VALUES (?,?,?,?,?,?,?,?)";
		System.out.println("insert SQL=" + SQL);

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, room.getOwnerId());
			preparedStatement.setString(2, room.getRoomName());
			preparedStatement.setString(3, room.getPassword());
			preparedStatement.setTimestamp(4, new java.sql.Timestamp(room
					.getStartDate().getTime()));
			preparedStatement.setTimestamp(5, new java.sql.Timestamp(room
					.getEndDate().getTime()));
			preparedStatement.setBoolean(6, room.getIsPolls());
			preparedStatement.setBoolean(7, room.getIsQuestions());
			preparedStatement.setBoolean(8, room.isAutorized());
			preparedStatement.executeUpdate();

			System.out.println("Created Record " + ROOMS_TABLE);

			id = getRoomSerialNum(room.getRoomName(), room.getPassword());
		} catch (SQLException ex) {
			System.err.println("error with inserting record to " + ROOMS_TABLE
					+ ": " + ex.getMessage());
		}

		return id;
	}

	private void initScheduledExecuter() {
		ScheduledExecutorService ses = Executors
				.newSingleThreadScheduledExecutor();
		// execute every 30 minutes
		System.out
				.println("scheduled event created:  every 30 minutes to delete expired rooms");
		ses.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				deleteExpiredRooms();
			}
		}, 0, 30, TimeUnit.MINUTES);

	}

	@Override
	public void updateRoomData(String name, String end, String start, int roomId) {
		if (name != null && !name.trim().isEmpty()) {
			updateRoomName(name, roomId);
		}
		if (start != null && !start.trim().isEmpty()) {
			updateTimeInRoom(start, "start_date", roomId);
		}
		if (end != null && !end.trim().isEmpty()) {
			updateTimeInRoom(end, "end_date", roomId);
		}
	}

	private void updateTimeInRoom(String date, String coulmnName, int roomId) {
		reOpenDBConn();

		String SQL = "UPDATE " + ROOMS_TABLE + " SET " + coulmnName
				+ " = ? WHERE serial_num  =?";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setTimestamp(1, new java.sql.Timestamp(
					DateTimeUtils.formatStringToDate(date).getTime()));
			preparedStatement.setInt(2, roomId);
			preparedStatement.executeUpdate();

			System.out.println("update Record in " + ROOMS_TABLE
					+ "serial_num = " + roomId + " in column= " + coulmnName
					+ ", date = " + date);
		}

		catch (SQLException ex) {
			System.err.println("error with updating record in " + ROOMS_TABLE
					+ ": " + ex.getMessage());
		}
	}

	public void updateIsAutorized(int roomId, boolean isAutorized) {
		reOpenDBConn();

		String SQL = "UPDATE " + ROOMS_TABLE + " SET isAutorized = ? "
				+ "WHERE serial_num  = " + roomId;
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setBoolean(1, isAutorized);
			preparedStatement.executeUpdate();

			System.out.println("update Record in " + ROOMS_TABLE
					+ "serial_num = " + roomId + " isAutorized = "
					+ isAutorized);
		}

		catch (SQLException ex) {
			System.err.println("error with updating record in " + ROOMS_TABLE
					+ ": " + ex.getMessage());
		}

	}

	private void updateRoomName(String name, int roomId) {
		reOpenDBConn();

		String SQL = "UPDATE " + ROOMS_TABLE + " SET room_name = ? "
				+ "WHERE serial_num  = " + roomId;
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setString(1, name);
			preparedStatement.executeUpdate();

			System.out.println("update Record in " + ROOMS_TABLE
					+ "serial_num = " + roomId + " room name = " + name);
		}

		catch (SQLException ex) {
			System.err.println("error with updating record in " + ROOMS_TABLE
					+ ": " + ex.getMessage());
		}
	}

	@Override
	public void deleteRoom(int roomId) {
		reOpenDBConn();

		String SQL = "DELETE FROM " + ROOMS_TABLE + " WHERE serial_num = ?";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			System.out.println("DELETE action, SQL=" + SQL);
			int delete = preparedStatement.executeUpdate();
			if (delete == 1) {
				System.out.println("room with room id = " + roomId
						+ " was deleted.");
			} else {
				System.out.println("There is matching room to this id: "
						+ roomId);
			}
		} catch (SQLException ex) {
			System.err
					.println("SQL statement: deleteing room with was not executed! : "
							+ ex.getMessage());
		}
	}

	@Override
	public List<Room> getAllRoomsByOwner(int ownerId) {
		reOpenDBConn();

		List<Room> roomList = new ArrayList<Room>();
		String SQL = "SELECT distinct " + ROOMS_TABLE + ".* FROM "
				+ ROOMS_TABLE + " WHERE owner_id = ? ORDER BY start_date ASC";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, ownerId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					roomList.add(mapRoomsToList(resultSet));
					System.out
							.println("result of query is rooms list in size of : "
									+ roomList.size()
									+ " owned by user serial number: "
									+ ownerId);
				}
			}

		} catch (SQLException ex) {
			System.err
					.println("SQL statement selecting all users in room wasn't exscuted : "
							+ ex.getMessage());
		}

		System.out
				.println("SQL statement selecting all users in room was exscuted.");
		return roomList;
	}

	@Override
	public Boolean checkJoinRoomValid(int roomId, String password) {
		Room room = getRoomByNumber(roomId);

		boolean isEqual;

		if ((password == room.getPassword()) && (room.isAutorized() == true)) {
			isEqual = true;
		} else {
			isEqual = false;
		}

		return isEqual;

	}

	@Override
	public Room getRoomByNumber(int roomId) {
		reOpenDBConn();
		Room room = null;

		String SQL = "SELECT " + ROOMS_TABLE + ".* FROM " + ROOMS_TABLE
				+ " WHERE serial_num  = " + roomId;
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					room = mapRoomsToList(resultSet);
				}

				System.out.println("select Record in " + ROOMS_TABLE
						+ ":: room serial_num = " + roomId);
			}
		}

		catch (SQLException ex) {
			System.err.println("error with selecting record in " + ROOMS_TABLE
					+ ": " + ex.getMessage());
		}

		return room;
	}

	@Override
	public void insertFeaturesToRoom(int roomId, boolean isPolls,
			boolean isQuestions) {
		reOpenDBConn();

		String SQL = "UPDATE " + ROOMS_TABLE
				+ " SET isPolls = ? ,isQuestions = ? " + "WHERE serial_num  = "
				+ roomId;
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setBoolean(1, isPolls);
			preparedStatement.setBoolean(2, isQuestions);
			preparedStatement.executeUpdate();

			System.out.println("update Record in " + ROOMS_TABLE
					+ ":: room serial_num = " + roomId + " polls feature = "
					+ isPolls + " questions feature = " + isQuestions);
		}

		catch (SQLException ex) {
			System.err.println("error with updating featurs of record in "
					+ ROOMS_TABLE + ": " + ex.getMessage());
		}
	}

	/**
	 * Delete expired rooms with timer of every 30 minutes delete from rooms
	 * where timestamp < NOW()
	 */
	private void deleteExpiredRooms() {
		reOpenDBConn();

		try (Statement statement = conn.createStatement();) {
			String SQL = "DELETE FROM " + ROOMS_TABLE
					+ " WHERE end_date < NOW()";
			System.out.println("DELETE action, SQL=" + SQL);
			int delete = statement.executeUpdate(SQL);
			if (delete == 1) {
				System.out.println("Expired rooms were deleted.");
			} else {
				System.out.println("There are no expired rooms.");
			}
		} catch (SQLException ex) {
			System.err.println("SQL statement: deleteing rooms with"
					+ "end_date < now is not executed! : " + ex.getMessage());
		}
	}

	private Room mapRoomsToList(ResultSet rs) throws SQLException {
		Room room;
		try {
			int roomNumber = rs.getInt("serial_num");
			int ownerId = rs.getInt("owner_id");
			String roomName = rs.getString("room_name");
			String password = rs.getString("password");
			Date start = rs.getTimestamp("start_date");
			Date end = rs.getTimestamp("end_date");
			boolean isPolls = rs.getBoolean("isPolls");
			boolean isQuestions = rs.getBoolean("isQuestions");
			boolean isAutorized = rs.getBoolean("isAutorized");
			room = new Room(roomNumber, ownerId, password, roomName, start,
					end, isPolls, isQuestions, isAutorized);
		} catch (SQLException ex) {
			throw (ex);
		}

		return room;
	}

	private int getRoomSerialNum(String name, String password)
			throws SQLException {

		int id = -1;
		reOpenDBConn();

		String SQL = "SELECT serial_num FROM " + ROOMS_TABLE
				+ " WHERE room_name = ? AND password = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, password);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					id = resultSet.getInt("serial_num");
				}
				System.out.println("room serial number:: " + id);
			}
		} catch (SQLException ex) {
			throw ex;
		}
		
		return id;
	}

	private void alterSerialNumbers() throws SQLException {
		String SQL = "ALTER TABLE " + ROOMS_TABLE + " AUTO_INCREMENT=1000;";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.executeUpdate();
			System.out
					.println("generate sequence for room serial num starting from 1000 ---> "
							+ ROOMS_TABLE);
		} catch (SQLException ex) {
			throw ex;
		}

	}

	private void reOpenDBConn() {
		this.conn = DBcloser.reOpenConnDB(conn);
	}
}
