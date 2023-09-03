package DataOperations;

import CustomExceptions.OperationException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static DataOperations.CRUD.getColNames;
import static DataOperations.CRUD.insertRow;

public class ScoreOperations {
    public static boolean addingFunds(Connection connection, String scoreNumber, double deposit) throws SQLException {
        boolean result = false;
        Statement statement = null;
        try {
            statement = connection.createStatement();

            statement.executeUpdate("BEGIN; " +
                    "UPDATE postgres.cleverbank.scores SET balance = balance + (" + deposit + ")::money " +
                    "WHERE score_number = '" + scoreNumber + "'::text; " +
                    "COMMIT;");

            result = true;
        } catch (SQLException e) {
            System.out.println("The account replenishment operation failed");
            statement.execute("ROLLBACK;");
            statement.close();
        } finally {
            statement.close();
            return result;
        }
    }

    public static boolean trasfer(Connection connection, String inputScore, String outputScore, double deposit) throws SQLException {
        boolean result = false;

        Statement statement = null;
        ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> colNames = getColNames(connection, "transactions");
        colNames.remove(0);
        LocalDateTime localDateTime = LocalDateTime.now();
        data.add(Double.toString(deposit));
        data.add("" + localDateTime.getYear() + "-" + "" + localDateTime.getMonth() +
                "-" + localDateTime.getDayOfMonth() + " " + localDateTime.getHour() +
                ":" + localDateTime.getSecond());
        data.add(outputScore);
        data.add(inputScore);

        try {
            statement = connection.createStatement();
            String command = "BEGIN; " +
                    "UPDATE postgres.cleverbank.scores SET balance = balance + (" + (-1) * deposit + ")::money " +
                    "WHERE score_number = '" + outputScore + "'::text; " +
                    "UPDATE postgres.cleverbank.scores SET balance = balance + (" + deposit + ")::money " +
                    "WHERE score_number = '" + inputScore + "'::text; COMMIT;";
            float outputbalance = getBalance(connection, outputScore);
            if (deposit <= outputbalance) {
                statement.executeUpdate(command);

                insertRow(connection, data, "transactions", colNames);
            } else {
                System.out.println("Amount being debited exceeds the score balance");
            }

            result = true;
            System.out.println("Transfer was complite");


        } catch (SQLException e) {
            statement.executeUpdate("ROLLBACK;");
        } finally {
            statement.close();
            return result;
        }
    }

    public static float getBalance(Connection connection, String scoreNumber) throws SQLException {
        PreparedStatement preparedStatement = null;
        float balance = 0.0F;
        try {
            String c = "SELECT cleverbank.scores.balance::numeric(10, 2) FROM cleverbank.scores WHERE score_number = '" + scoreNumber + "';";
            preparedStatement = connection.prepareStatement(c);

            // Выполняем запрос и получаем результат
            ResultSet resultSet = preparedStatement.executeQuery();

            // Получаем метаданные результата
            resultSet.next();
            Object columnValue = resultSet.getObject(1);
            balance = Float.valueOf(columnValue.toString());
        } catch (SQLException e) {
            System.out.println("Getting the score balance failed");
        } finally {
            preparedStatement.close();
        }
        return balance;
    }
}