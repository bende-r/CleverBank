package DataOperations;

import CustomExceptions.OperationException;

import java.sql.*;

public class ScoreOperations {
    public static void addingFunds(Connection connection, String scoreNumber, float deposit) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            //  if (deposit <= 0) throw new OperationException("Incorrect deposit amount");

            statement.executeUpdate("BEGIN; " +
                    "UPDATE postgres.cleverbank.scores SET balance = balance + (" + deposit + ")::money " +
                    "WHERE score_number = " + scoreNumber + "::text; " +
                    "COMMIT;");

        } catch (SQLException e) {
            System.out.println("The account replenishment operation failed");
            statement.execute("ROLLBACK;");
            statement.close();
        }
      /*  catch (OperationException e) {
            System.out.println(e.getMessage());
        } */ finally {
            statement.close();
        }
    }

    public static float getBalance(Connection connection, String scoreNumber) throws SQLException {
        PreparedStatement preparedStatement = null;
        float columnValue = 0;
        try {
            String c = "SELECT cleverbank.scores.balance FROM cleverbank.scores WHERE score_number = '" + scoreNumber + "';";
            preparedStatement = connection.prepareStatement(c);

            // Выполняем запрос и получаем результат
            ResultSet resultSet = preparedStatement.executeQuery();

            // Получаем метаданные результата
            resultSet.next();
            columnValue = (float) resultSet.getObject(1);

        } catch (SQLException e) {
            System.out.println("Getting the score balance failed");
        } finally {
            preparedStatement.close();
        }
        return columnValue;
    }

}
