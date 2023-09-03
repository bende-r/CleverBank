package JDBC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс, реализующий метод, считывающий и исполняюший SQL-файл
 *
 * @author Богдан Рыбаков
 * @version 1.0
 */
public class SQLFileExecuter {

    /**
     * Метод, считывающий и исполняющий SQL-файл
     *
     * @param path       - путь к файлу
     * @param connection - подключение к базе данных
     * @throws SQLException
     */
    public static void sqlFileExecuter(String path, Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
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
            System.out.println("Request execution failed");
        } catch (IOException e) {
            System.out.println("File reading failed");
        } finally {
            statement.close();
        }
    }
}

