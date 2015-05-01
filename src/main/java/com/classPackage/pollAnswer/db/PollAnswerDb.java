package com.classPackage.pollAnswer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.classPackage.dbUtils.DBcloser;
import com.classPackage.pollAnswer.logic.PollAnswer;

public class PollAnswerDb implements PollAnswerDAO {
	private Connection conn;
	private static final String POLL_ANSWERS_TABLE = "poll_answers";
	private static final String POLLS_TABLE = "polls";

	public PollAnswerDb(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable() {
		try (Statement st = conn.createStatement();) {
			String SQL = " CREATE TABLE IF NOT EXISTS " + POLL_ANSWERS_TABLE;
			SQL += " ( ";
			SQL += " serial_num MEDIUMINT NOT NULL AUTO_INCREMENT, ";
			SQL += " user_id MEDIUMINT NOT NULL, ";
			SQL += " answer MEDIUMINT NOT NULL, ";
			SQL += " FOREIGN KEY (serial_num) REFERENCES " + POLLS_TABLE
					+ "(serial_num), ";
			SQL += " PRIMARY KEY (serial_num)";
			SQL += " ) ";
			SQL += " ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin; ";
			st.executeUpdate(SQL);
			System.out.println("Created the table ---> " + POLL_ANSWERS_TABLE);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}

	@Override
	public void insertpollAnswerRecord(PollAnswer pollAnswer) {
		String SQL = "INSERT INTO " + POLL_ANSWERS_TABLE
				+ " (poll_id, user_id, answer)" + " VALUES (?,?,?);";
		System.out.println("insert SQL=" + SQL);
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, pollAnswer.getPollId());
			preparedStatement.setInt(2, pollAnswer.getUserId());
			preparedStatement.setInt(3, pollAnswer.getAnswer());

			preparedStatement.executeUpdate();
			preparedStatement.close();
			System.out.println("Created Record to " + POLL_ANSWERS_TABLE);

		} catch (SQLException ex) {
			System.err.println("error with inserting record to "
					+ POLL_ANSWERS_TABLE + ": " + ex.getMessage());

		}
	}

	@Override
	public int numberOfPollsInRoom(int roomId) {
		int count = 0;
		reOpenDBConn();
		String SQL = " SELECT COUNT (poll_id) as count " + " FROM "
				+ POLL_ANSWERS_TABLE + " WHERE room_id = ? ";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					count = resultSet.getInt("count");
				}
			}
		} catch (SQLException ex) {
			System.err
					.println("error in counting number of polls in room query: "
							+ ex.getMessage());
		}
		System.out
				.println("SQL statement selecting number of Parcipents in poll was exscuted.");
		
		return count;
	}

	@Override
	public int numberOfParcipentsInPoll(int roomId, int pollId) {
		int count = 0;
		reOpenDBConn();

		String SQL = " SELECT COUNT (serial_num) as count " + " FROM "
				+ POLL_ANSWERS_TABLE + " WHERE room_id = ? , poll_id = ? ";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			preparedStatement.setInt(2, pollId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					count = resultSet.getInt("count");
				}
			}
		} catch (SQLException ex) {
			System.err
					.println("error in counting number of Parcipents in poll query: "
							+ ex.getMessage());
		}

		System.out
				.println("SQL statement selecting number of Parcipents in poll was exscuted.");
		return count;
	}

	@Override
	public double getAverageOfPollAnswer(int roomId, int pollId) {
		double avg = 0;

		reOpenDBConn();
		String SQL = " SELECT AVG (answer) as avg " + " FROM "
				+ POLL_ANSWERS_TABLE + " WHERE room_id = ? , poll_id = ? ";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			preparedStatement.setInt(2, pollId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					avg = resultSet.getDouble("avg");
				}
			}
		} catch (SQLException ex) {
			System.err.println("error in Avg pollAnswer query: "
					+ ex.getMessage());
		}

		System.out
				.println("SQL statement selecting rate average of all answers in poll was exscuted.");
		return avg;
	}

	private void reOpenDBConn() {
		this.conn = DBcloser.reOpenConnDB(conn);
	}
}