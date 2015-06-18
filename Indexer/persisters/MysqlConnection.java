package Indexer.persisters;

import config.MysqlConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlConnection {

    private static Connection connection = null;

    private MysqlConnection() {
        //
    }

    /**
     * Return single MySQL connection
     *
     * @return Connection object
     */
    public static Connection getConnection() {
        if(connection == null) {
            connection = initConnection();
        }
        return connection;
    }

    /**
     * Initialize MySQL connection.
     *
     * @return Connection object
     */
    private static Connection initConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + MysqlConfig.DB_NAME.toString() + "?characterEncoding=UTF-8" +
                    "&user=" + MysqlConfig.DB_USER.toString() +
                    "&password=" + MysqlConfig.DB_PASS.toString()
            );
        } catch (/*SQL*/Exception e) {
            System.err.println("Cannot connect ot database.");
        }

        return connection;
    }
}
