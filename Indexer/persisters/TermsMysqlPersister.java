package Indexer.persisters;

import java.sql.*;
import java.util.Map;
import java.util.Set;

public class TermsMysqlPersister {

    public TermsMysqlPersister() {
        //
    }

    /**
     * Save into database given word/term.
     *
     * @param word given term
     * @param type type - word or term
     *
     * @return the id of the word/term
     */
    public Long saveTerm(String word, TermType type) {
        Long id = 0L;
        String checkSql = "SELECT id FROM terms WHERE term = ? AND type = ";
        String insertSql = "INSERT INTO terms(term, type) VALUES(?, ?)";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement checkStatement = mysqlConnection.prepareStatement(checkSql);
            PreparedStatement insertStatement = mysqlConnection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);



            insertStatement.setNString(1, word);
            insertStatement.setString(2, type.toString());
            insertStatement.executeUpdate();

            id = getLastInsertedId(insertStatement);
        } catch (SQLException ex) {
            System.err.println("SQL error on saving " + type.toString() + " " + word + ": " + ex.getMessage());
        }

        return id;
    }

    /**
     * Save all terms and their relations to given word
     *
     * @param terms  the id of the term
     * @param wordId the id of the word
     */
    public void saveTerms(Map<String, Integer> terms, Long wordId) {
        terms.forEach((term, editDistance) -> {
            Long termId = saveTerm(term, TermType.TERM);
            saveRelation(termId, wordId, editDistance);
        });
    }

    /**
     * Save relations between terms and words.
     *
     * @param termId       the id of the term
     * @param wordId       the id of the word
     * @param editDistance count of deletions that are made to get the term from the word
     */
    private void saveRelation(Long termId, Long wordId, Integer editDistance) {
        String sql = "INSERT INTO relations(word_id, term_id, edit_distance) VALUES(?, ?, ?)";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setLong(1, wordId);
            sqlStatement.setLong(2, termId);
            sqlStatement.setInt(3, editDistance);

            sqlStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("SQL error on saving relation between term " + termId + " and word " + wordId + ": " + ex.getMessage());
        }
    }

    /**
     * Return the id of last inserted word/term
     *
     * @param sqlStatement the PreparedStatement that were used to insert the word/term
     *
     * @return the is as Long
     *
     * @throws SQLException if something went wrong
     */
    private Long getLastInsertedId(PreparedStatement sqlStatement) throws SQLException {
        Long key = 0L;

        ResultSet rs = sqlStatement.getGeneratedKeys();
        if (rs != null && rs.next()) {
            key = rs.getLong(1);
        }

        return key;
    }
}
