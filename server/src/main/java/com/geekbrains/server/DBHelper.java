package com.geekbrains.server;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBHelper implements AutoCloseable {

    private static DBHelper instance;
    private static Connection connection;

    private static PreparedStatement findByLoginAndPassword;
    private static PreparedStatement changeNick;
    private static final Logger logger = Logger.getLogger(DBHelper.class.getName());

    private DBHelper() {}

    public static DBHelper getInstance() {
        if (instance == null) {
            loadDriverOpenConnection();
            createPreparedStatements();

            instance = new DBHelper();
        }
        return instance;
    }

    private static void loadDriverOpenConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:UserDB.db");
        } catch (ClassNotFoundException | SQLException e) {
//            System.err.println("Ошибка соединения с базой данных");
            logger.log(Level.WARNING, "Ошибка соединения с базой данных");
            e.printStackTrace();
        }
    }

    private static void createPreparedStatements() {
        try {
            findByLoginAndPassword = connection.prepareStatement("SELECT * FROM user_data WHERE LOWER(login)=LOWER(?) AND password = ?");
            changeNick = connection.prepareStatement("UPDATE user_data SET nickname=? WHERE nickname=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String findByLoginAndPassword(String login, String password) {
        ResultSet resultSet = null;

        try {
            findByLoginAndPassword.setString(1, login);
            findByLoginAndPassword.setString(2, password);

            resultSet = findByLoginAndPassword.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }
        return null;
    }

    private void closeResultSet(ResultSet resultSet) {
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int updateNickname(String oldNickname, String newNickname) {
        try {
            changeNick.setString(1, newNickname);
            changeNick.setString(2, oldNickname);

            return changeNick.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void close() {
        try {
            findByLoginAndPassword.close();
            changeNick.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
