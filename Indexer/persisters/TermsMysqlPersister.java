package Indexer.persisters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TermsMysqlPersister {

    public TermsMysqlPersister() {
        //
    }

    public void saveWord(String word) {
        // TODO: finish the SQL
        String sql = "INSERT INTO terms() VALUES()";

        try (
            Connection mysqlConnection = MysqlConnection.getConnection();
            PreparedStatement sqlStatement = mysqlConnection.prepareStatement(sql)
        ) {
//            sqlStatement.setString(1, value);

            try (ResultSet resultSet = sqlStatement.executeQuery()) {
                while(resultSet.next()) {
                    //token =  new FacebookPageAccessTokenEntity(resultSet.getString("access_token"), resultSet.getLong("id"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("SQL error on saving word " + word);
        }
    }

    public void saveTerms() {
        // TODO
    }
}
