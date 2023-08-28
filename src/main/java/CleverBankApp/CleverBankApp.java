package CleverBankApp;

import JDBC.SQLFileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class CleverBankApp {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/";
    static final String USER = "postgres";
    static final String PASS = "postgres";

    public static void main(String[] argv) {

        SQLFileReader sqlFileReader = new SQLFileReader();

        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();

            String path = new File("src/main/sql/createdb.sql").getAbsolutePath();

            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            StringBuilder sqlQuery = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sqlQuery.append(line);

                // Если строка завершается точкой с запятой, выполните SQL-запрос
                if (line.trim().endsWith(";")) {
                    String query = sqlQuery.toString();
                    statement.executeUpdate(query);

                    // Очистите буфер для следующего запроса
                    sqlQuery.setLength(0);
                }
            }

            // Закрываем ресурсы
            reader.close();
            statement.close();
            connection.close();

            System.out.println("SQL-файл успешно выполнен.");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }







       /* Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) System.out.println("You successfully connected to database now");
        else {
            System.out.println("Failed to make connection to database");
        }

        try {
            Statement statement = connection.createStatement();
            //  String basePath = new File("").getAbsolutePath();
            //  System.out.println(basePath);

            String path = new File("src/main/sql/createdb.sql").getAbsolutePath();
            System.out.println(path);

            String comand = sqlFileReader.readFileAsString(path);
            statement.execute(comand);
        } catch (SQLException e) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
*/
    }
}
