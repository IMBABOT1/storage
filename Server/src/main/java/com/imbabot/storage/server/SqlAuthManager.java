package com.imbabot.storage.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqlAuthManager implements AuthManager{
    private static Connection connection;
    private static Statement stmt;


    public static void connect(){
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:dbmain.db");
    }
    public static void disconnect(){

    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {
        return null;
    }
}
