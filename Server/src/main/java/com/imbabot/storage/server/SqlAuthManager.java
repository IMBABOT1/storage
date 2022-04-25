package com.imbabot.storage.server;

import java.sql.*;

public class SqlAuthManager implements AuthManager{
    private Connection connection;
    private PreparedStatement ps;

    @Override
    public void connect()  {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:dbmain.db");
            ps = connection.prepareStatement("SELECT name FROM users WHERE login = ? AND pass = ?");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Unable to connect");
        }
    }

    @Override
    public void close() {
        if (ps != null){
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (connection != null){
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {
        String result = "";
        try {
            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()){
                while (!rs.next()){
                    return null;
                }
                result = rs.getString(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to get name by login/password");
        }
        return result;
    }
}