package Indexer.persisters;

import Indexer.storage.MysqlConnection;

import java.sql.*;
import java.util.*;

public class TermsMysqlPersister {

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
        String insertSql = "INSERT INTO terms(term, type) VALUES(?, ?)";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
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
     * Save relations between terms and words.
     *
     * @param termId       the id of the term
     * @param wordId       the id of the word
     * @param editDistance count of deletions that are made to get the term from the word
     */
    public void saveRelation(Long termId, Long wordId, Integer editDistance) {
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

    /**
     * Fetch term's data by given word string.
     *
     * @param word searched term
     *
     * @return HasMap of id and type of the term (or empty HasMap if the term is not found)
     */
    public Map<String, Object> fetchTerm(String word) {
        Map<String, Object> result = new HashMap<>();
        String sql = "SELECT id, type FROM terms WHERE term = ?";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setString(1, word);

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                result.put("id", resultSet.getLong("id"));
                result.put("type", resultSet.getString("type"));
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on fetching term " + word + " : " + ex.getMessage());
        }

        return result;
    }

    /**
     * Change type of given term to 'word'.
     *
     * @param wordId id of the term
     */
    public void convertTermToWord(Long wordId) {
        String sql = "UPDATE terms SET type = ? WHERE id = ?";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setString(1, TermType.WORD.toString());
            sqlStatement.setLong(2, wordId);

            sqlStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("SQL error on converting term " + wordId + " to word: " + ex.getMessage());
        }
    }

    // TODO: documentation
    public Set<Long> getInheritedRelations(Long termId, Integer editDistance) {
        Set<Long> result = new HashSet<>();
        String sql = "" +
            "SELECT terms.id " +
            "FROM terms " +
            "JOIN relations " +
                "ON terms.id = relations.term_id " +
            "WHERE " +
                "relations.word_id = ? " +
                "AND relations.edit_distance = ?";

        try  {
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql);

            sqlStatement.setLong(1, termId);
            sqlStatement.setInt(2, editDistance);

            ResultSet resultSet = sqlStatement.executeQuery();

            while(resultSet.next()) {
                result.add(resultSet.getLong("id"));
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on fetching inherited relations of term " + termId + " : " + ex.getMessage());
        }

        return result;
    }
}
