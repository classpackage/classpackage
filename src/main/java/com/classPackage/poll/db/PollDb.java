package com.classPackage.poll.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.classPackage.dbUtils.DBcloser;
import com.classPackage.poll.logic.poll;

public class PollDb implements PollDAO {

	private Connection conn;
	private static final String POLLS_TABLE = "polls";
	private static final String ROOMS_TABLE = "rooms";

	public PollDb(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable() {
		try (Statement st = conn.createStatement();) {
			String SQL = " CREATE TABLE IF NOT EXISTS " + POLLS_TABLE;
			SQL += " ( ";
			SQL += " serial_num MEDIUMINT NOT NULL AUTO_INCREMENT, ";
			SQL += " room_id MEDIUMINT NOT NULL, ";
			SQL += " ordering MEDIUMINT NOT NULL, ";
			SQL += " PRIMARY KEY (serial_num), ";
			SQL += " FOREIGN KEY (room_id) REFERENCES " + ROOMS_TABLE
					+ "(serial_num) ";
			SQL += " ON DELETE CASCADE";
			SQL += " ) ";
			SQL += " ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin; ";
			st.executeUpdate(SQL);
			System.out.println("Created the table ---> " + POLLS_TABLE);
		} catch (SQLException ex) {
			System.err.println("error in creating table: " + POLLS_TABLE
					+ " :: " + ex.getMessage());
		}
	}

	@Override
	public void insertPollRecord(poll poll) {
		reOpenDBConn();
		String SQL = "INSERT INTO " + POLLS_TABLE + " (room_id, ordering)"
				+ " VALUES (?,?);";
		System.out.println("insert SQL=" + SQL);

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, poll.getRoomId());
			preparedStatement.setInt(2, poll.getOrdering());

			preparedStatement.executeUpdate();
			preparedStatement.close();
			System.out.println("Created Record to " + POLLS_TABLE);

		} catch (SQLException ex) {
			System.err.println("error with inserting record to " + POLLS_TABLE
					+ ": " + ex.getMessage());
		}
	}

	@Override
	public void deletePoll(int roomId, int ordering) {
		reOpenDBConn();
		String SQL = "DELETE FROM " + POLLS_TABLE
				+ " WHERE room_id = ? AND ordering = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			preparedStatement.setInt(2, ordering);
			System.out.println("DELETE action, SQL=" + SQL);
			int delete = preparedStatement.executeUpdate();
			if (delete == 1) {
				System.out.println("Row:ordering " + ordering + " in room id"
						+ roomId + "is deleted.");
			} else {
				System.out.println("Row:ordering " + ordering + " in room id"
						+ roomId + "is not deleted.");
			}
		} catch (SQLException ex) {
			System.err.println("error with deliting poll from " + POLLS_TABLE
					+ ": " + ex.getMessage());
		}
	}

	@Override
	public List<poll> getAllPollsInRoom(int roomId) {
		List<poll> pollsInRoomList = new ArrayList<poll>();
		reOpenDBConn();

		String SQL = "SELECT distinct " + POLLS_TABLE + ".* FROM "
				+ POLLS_TABLE + " WHERE room_id = '" + roomId + "'";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				preparedStatement.setInt(1, roomId);
				while (resultSet.next()) {
					pollsInRoomList.add(mapPollsToList(resultSet));
					System.out
							.println("result of query is polls list in size of : "
									+ pollsInRoomList.size());
				}
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		System.out
				.println("SQL preparedStatement selecting all polls in room was exscuted.");

		return pollsInRoomList;
	}

	/*
	 * Creates a poll object from ResultSet that will be inserted to
	 * 'pollsInRoom' list -WORKS returns pollsInRoom
	 */

	private poll mapPollsToList(ResultSet rs) throws SQLException {
		poll poll;
		try {
			int roomId = rs.getInt("room_id");
			int ordering = rs.getInt("ordering");
			poll = new poll(roomId, ordering);
		} catch (SQLException ex) {
			throw (ex);
		}
		return poll;
	}

	private void reOpenDBConn() {
		this.conn = DBcloser.reOpenConnDB(conn);
	}
}