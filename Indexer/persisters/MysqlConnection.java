package Indexer.persisters;

import config.MysqlConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
     * @return Connnection object
     */
    private static Connection initConnection() {
        Connection connection = null;

        try {
            connection =  DriverManager.getConnection(
                "jdbc:mysql://localhost/" + MysqlConfig.DB_NAME + "?" +
                    "user=" + MysqlConfig.DB_USER +
                    "&password=" + MysqlConfig.DB_PASS
            );
        } catch (SQLException e) {
            System.err.println("Cannot connect ot database.");

        }

        return connection;
    }
}
