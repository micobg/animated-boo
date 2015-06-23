package persisters;

import storage.RedisConnector;
import redis.clients.jedis.Jedis;

public class TermsRedisPersister {

    private static final String WORDS_SET = "words";

    /**
     * Insert given word/term into Redis.
     *
     * @param word the term
     * @param type type of the term - word or term
     *
     * @return numbers of inserted values (1 if the term is new in the set and 0 if it has been already added)
     */
    public Long saveTerm(String word, TermType type) {
        //Connecting to Redis server on localhost
        Jedis redis = RedisConnector.getConnection();

        return redis.sadd(WORDS_SET, word);

//        ScanResult<String> list = redis.sscan("test", "0");
//        list.getResult().forEach((s) -> {
//            System.out.println("Stored string in redis:: " + s);
//        });
    }
}
