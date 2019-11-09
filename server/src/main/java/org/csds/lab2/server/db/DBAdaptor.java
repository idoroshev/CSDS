package org.csds.lab2.server.db;

import java.sql.*;

public class DBAdaptor {

    private static final String GET_FILE_TEXT = "SELECT text FROM Files WHERE name = ?;";
    private static final String UPDATE_FILE_TEXT = "UPDATE Files SET text = ? WHERE name = ?";
    private static final String INSERT_FILE = "INSERT INTO Files(name, text) VALUES (?, ?)";

    private Connection connection;

    public DBAdaptor(String dbUrl) {
        try {
            connection = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            System.out.println("Cannot connect to DB: " + e.getMessage());
        }
    }

    public String getFileText(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FILE_TEXT);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            return rs.getString("text");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean insertFile(String name, String text) {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_FILE_TEXT);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (!rs.isClosed() && rs.getString("text") != null) {
                PreparedStatement updateStatement = connection.prepareStatement(UPDATE_FILE_TEXT);
                updateStatement.setString(1, text);
                updateStatement.setString(2, name);
                int updateResult = updateStatement.executeUpdate();
                return updateResult > 0;
            } else {
                PreparedStatement insertStatement = connection.prepareStatement(INSERT_FILE);
                insertStatement.setString(1, name);
                insertStatement.setString(2, text);
                int insertResult = insertStatement.executeUpdate();
                return insertResult > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
