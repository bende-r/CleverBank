package DataOperations;

import CustomExceptions.OperationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ScoreOperations {
    public static void addingFunds(Connection connection, String scoreNumber, int deposit) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            if (scoreNumber.length() != 10) throw new OperationException("Incorrect score number");
            if (deposit <= 0) throw new OperationException("Incorrect deposit amount");

            statement.executeUpdate("BEGIN; " +
                    "UPDATE postgres.cleverbank.scores SET balance = balance + " + deposit + "::money " +
                    "WHERE score_number = " + scoreNumber + "::text; " +
                    "COMMIT;");

            statement.close();
        } catch (SQLException e) {
            System.out.println("The account replenishment operation failed");
            statement.execute("ROLLBACK;");
            statement.close();
        } catch (OperationException e) {
            System.out.println(e.getMessage());
        }
    }

}
