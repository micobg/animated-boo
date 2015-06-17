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
     */
    public Long saveTerm(String word, TermType type) {
        Long id = 0L;
        String sql = "INSERT INTO terms(term, type) VALUES(?, ?)";

        try (
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            sqlStatement.setString(1, word);
            sqlStatement.setString(2, type.toString());

            sqlStatement.executeQuery();

            id = getLastInseretedId(sqlStatement);
        } catch (SQLException ex) {
            System.err.println("SQL error on saving " + type.toString() + " " + word);
        }

        return id;
    }

    public void saveTerms(Map<String, Integer> terms, Long wordId) {
        terms.forEach((term, editDistance) -> {
            Long termId = saveTerm(term, TermType.TERM);
            saveRelation(termId, wordId, editDistance);
        });
    }

    private void saveRelation(Long termId, Long wordId, Integer editDistance) {
        String sql = "INSERT INTO relations(word_id, term_id, edit_distance) VALUES(?, ?, ?)";

        try (
                Connection mysqlConnection = MysqlConnection.getConnection();
                PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql)
        ) {
            sqlStatement.setLong(1, wordId);
            sqlStatement.setLong(2, termId);
            sqlStatement.setInt(3, editDistance);

            sqlStatement.executeQuery();
        } catch (SQLException ex) {
            System.err.println("SQL error on saving relation between term " + termId + " and word " + wordId);
        }
    }

    private Long getLastInseretedId(PreparedStatement sqlStatement) throws SQLException {
        Long key = 0L;

        ResultSet rs = sqlStatement.getGeneratedKeys();
        if (rs != null && rs.next()) {
            key = rs.getLong(1);
        }

        return key;
    }
}
