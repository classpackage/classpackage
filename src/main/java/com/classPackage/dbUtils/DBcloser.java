package com.classPackage.dbUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBcloser {
	/**
	 * Closes the provided ResultSets
	 * 
	 * @param resultSets
	 *            ResultSets that should be closed
	 */
	public static void close(ResultSet... resultSets) {
		if (resultSets != null) {
			for (ResultSet resultSet : resultSets) {
				if (resultSet != null) {
					try {
						resultSet.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Closes the provided Statements
	 * 
	 * @param statements
	 *            Statements that should be closed
	 */
	public static void close(Statement... statements) {
		/*
		 * No need to create methods for PreparedStatement and
		 * CallableStatement, because they extend Statement.
		 */
		if (statements != null) {
			for (Statement statement : statements) {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Closes the provided Statements
	 * 
	 * @param statements
	 *            Statements that should be closed
	 */

	/**
	 * Closes the provided Connections
	 * 
	 * @param connections
	 *            Connections that should be closed
	 */
	public static void close(Connection... connections) {
		if (connections != null) {
			for (Connection connection : connections) {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static Connection reOpenConnDB(Connection conn) {
		try {
			if (conn.isClosed()) {
				System.out.println("connection was closed...reopening");
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return  DBConn.getConnection();
	}
}
