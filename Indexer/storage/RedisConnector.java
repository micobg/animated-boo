package Indexer.storage;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public final class RedisConnector {

    private static final Integer DB_NUMBER = 2;

    private static Jedis connection;

    private RedisConnector() {
    }

    public static Jedis getConnection() {
        if (connection == null) {
            try {
                connection = new Jedis("localhost");

                // select database
                connection.select(DB_NUMBER);
            } catch (JedisConnectionException ex) {
                System.err.println("Redis connect error: " + ex.getMessage());
            }
        }
        return connection;
    }
}