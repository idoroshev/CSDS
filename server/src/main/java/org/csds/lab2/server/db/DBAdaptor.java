package org.csds.lab2.server.db;

import java.sql.*;

public class DBAdaptor {

    private static final String GET_FILE_TEXT = "SELECT text FROM Files WHERE name = ?;";

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
}
