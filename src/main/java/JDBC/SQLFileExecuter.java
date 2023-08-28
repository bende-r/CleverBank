package JDBC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLFileExecuter {

  /*  public static void SQLFileExecute(String path, Statement statement) throws SQLException, IOException {

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
        reader.close();
    }*/

    public static void SQLFileExecuter(String path, Statement statement) {
        try {
            // Чтение SQL-скрипта из файла
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            StringBuilder script = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                script.append(line);
                script.append("\n");
            }
            reader.close();

            // Выполнение SQL-скрипта
            statement.execute(script.toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

