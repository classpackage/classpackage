package com.classPackage.question.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.classPackage.dbUtils.DBcloser;
import com.classPackage.question.logic.Question;
import com.classPackage.utils.DateTimeUtils;

public class QuestionDb implements QuestionDAO {

	private Connection conn;
	private static final String QUESTION_TABLE = "questions";
	private static final String USERS_IN_ROOM_TABLE = "users_in_room";
	private static final String ROOMS_TABLE = "rooms";

	public QuestionDb(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable() {
		try (Statement st = conn.createStatement();) {
			String SQL = " CREATE TABLE IF NOT EXISTS " + QUESTION_TABLE;
			SQL += " ( ";
			SQL += " serial_num MEDIUMINT NOT NULL AUTO_INCREMENT, ";
			SQL += " room_id MEDIUMINT NOT NULL, ";
			SQL += " owner_id MEDIUMINT NOT NULL, ";
			SQL += " question_asked VARCHAR(100) NOT NULL, ";
			SQL += " time_asked TIMESTAMP NOT NULL, ";
			SQL += " question_rating MEDIUMINT, ";
			SQL += " PRIMARY KEY (serial_num), ";
			SQL += " FOREIGN KEY (owner_id) REFERENCES " + USERS_IN_ROOM_TABLE
					+ "(user_id) ";
			SQL += " ON DELETE CASCADE, ";
			SQL += " FOREIGN KEY (room_id) REFERENCES " + ROOMS_TABLE
					+ "(serial_num) ";
			SQL += " ON DELETE CASCADE";
			SQL += " ) ";
			SQL += " ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_bin; ";
			st.executeUpdate(SQL);
			System.out.println("Created the table ---> " + QUESTION_TABLE);

		} catch (SQLException ex) {
			System.err.println("error in creating table: " + QUESTION_TABLE
					+ " :: " + ex.getMessage());
		}
		System.out.println("Created the " + QUESTION_TABLE + " succesfully");
	}

	@Override
	public int insertQuestionRecored(Question question) {
		int questionSerialNum = 0;

		reOpenDBConn();
		String SQL = "INSERT INTO "
				+ QUESTION_TABLE
				+ " (room_id, owner_id, question_asked, time_asked, question_rating)"
				+ " VALUES (?,?,?,?,?);";
		System.out.println("insert SQL=" + SQL);
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			Timestamp timeAsked = DateTimeUtils.getCurrentTimeStamp(question
					.getTimeQuestion());

			preparedStatement.setInt(1, question.getRoomId());
			preparedStatement.setInt(2, question.getOwnerId());
			preparedStatement.setString(3, question.getQuestionAsked());
			preparedStatement.setTimestamp(4, timeAsked);
			preparedStatement.setInt(5, question.getQuestionRating());

			preparedStatement.executeUpdate();
			preparedStatement.close();

			System.out.println("Created Record to " + QUESTION_TABLE);
			questionSerialNum = getQuestionSerialNum(question.getRoomId(),
					timeAsked);
			System.out.println("user was added to: " + QUESTION_TABLE
					+ " table, with serial number: " + questionSerialNum + ".");

		} catch (SQLException | ParseException ex) {
			System.err.println("error with inserting record to "
					+ QUESTION_TABLE + ": " + ex.getMessage());
		}

		return questionSerialNum;
	}

	@Override
	public void updateQuestionRating(int questionRating, int roomId,
			String timeAsked) {
		reOpenDBConn();
		String SQL = "UPDATE " + QUESTION_TABLE
				+ " SET question_rating = ?  WHERE serial_num = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			int questionId = getQuestionSerialNum(roomId,
					DateTimeUtils.getCurrentTimeStamp(timeAsked));

			preparedStatement.setInt(1, questionRating);
			preparedStatement.setInt(2, questionId);

			System.out.println("UPDATE action, SQL=" + SQL);
			int update = preparedStatement.executeUpdate();
			if (update == 1) {
				System.out.println("Row:id " + questionId + " is updated.");
			} else {
				System.out.println("Row:id " + questionId + " is not updated.");
			}

		} catch (SQLException | ParseException ex) {
			System.err.println("SQL statement: " + SQL
					+ " was not executed! :  " + ex.getMessage());
		}
	}

	@Override
	public List<Question> getAllQuesiotionsByCritetia(String columnName,
			int value) {
		List<Question> questionList = new ArrayList<Question>();

		reOpenDBConn();
		String SQL = "SELECT distinct " + QUESTION_TABLE + ".* FROM "
				+ QUESTION_TABLE + " WHERE ? = ? ORDER BY question_rating DESC";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setString(1, columnName);
			preparedStatement.setInt(2, value);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				while (resultSet.next()) {
					questionList.add(mapQuestionToList(resultSet));
					System.out
							.println("result of query is questions list in size of : "
									+ questionList.size()
									+ "by critiria column: "
									+ columnName
									+ " = " + value);
				}
			}

		} catch (SQLException ex) {
			System.err
					.println("SQL statement selecting questions by criteria wasn't exscuted : "
							+ ex.getMessage());
		}

		System.out
				.println("SQL statement selecting all users in room was exscuted.");
		return questionList;
	}

	@Override
	public int countQuestionInRoom(int roomId) {
		int questionCounter = 0;

		reOpenDBConn();
		String SQL = "SELECT count(*) FROM " + QUESTION_TABLE
				+ " WHERE room_id = ?";

		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet.next()) {
					questionCounter = resultSet.getInt(1);
					System.out.println("number Of question in room: " + roomId
							+ "is: " + questionCounter);
				} else {
					System.out
							.println("error: could not get the record counts");
				}
			}

		} catch (SQLException ex) {
			System.err
					.println("SQL statement counting question in room wasn't exscuted : "
							+ ex.getMessage());
		}

		return questionCounter;
	}

	private int getQuestionSerialNum(int roomId, Timestamp timeAsked) {
		int serialNum = -1;

		reOpenDBConn();
		String SQL = "SELECT serial_num FROM " + ROOMS_TABLE
				+ " WHERE room_id = ? AND time_asked = ?";
		try (PreparedStatement preparedStatement = conn.prepareStatement(SQL);) {
			preparedStatement.setInt(1, roomId);
			preparedStatement.setTimestamp(2, timeAsked);
			try (ResultSet resultSet = preparedStatement.executeQuery();) {
				if (resultSet != null && resultSet.next()) {
					serialNum = resultSet.getInt("serial_num");
				}
			}
		} catch (SQLException ex) {
			System.err.println("error in getting user's serial nummber. "
					+ ex.getMessage());
		}

		return serialNum;
	}

	private Question mapQuestionToList(ResultSet rs) throws SQLException {
		Question question = null;
		try {
			int questionId = rs.getInt("serial_num");
			int roomId = rs.getInt("room_id");
			int ownerId = rs.getInt("ownerId");
			String questionAsked = rs.getString("question_asked");
			int questionRating = rs.getInt("question_rating");
			String timeAsked = DateTimeUtils.getTimeStampAsString(rs
					.getTimestamp("time_asked"));
			question = new Question(questionId, roomId, ownerId, questionAsked,
					timeAsked, questionRating);
		} catch (SQLException ex) {
			throw (ex);
		}

		return question;
	}

	private void reOpenDBConn() {
		this.conn = DBcloser.reOpenConnDB(conn);
	}
}
