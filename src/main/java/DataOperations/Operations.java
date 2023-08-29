package DataOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Operations {
    public static boolean addingFunds(Statement statement, String scoreNumber, int deposit) {
        try {
            if (scoreNumber.length() != 10) throw new RuntimeException("Invalid account number length");
            if (deposit <= 0) throw new RuntimeException("Invalid deposit value");

            int resultSet = statement.executeUpdate("UPDATE  cleverbank.scores SET balance = balance + " + deposit + "::money WHERE score_number = " + scoreNumber + ";");
            return resultSet != 0;
        } catch (SQLException e) {
            System.out.println("The account replenishment operation failed");
            return false;
        }
    }
}
