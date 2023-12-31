package DataOperations;

import java.sql.*;
import java.util.ArrayList;

/**
 * Класс, реализующий универсальные методы CRUD для работы с базами данных
 *
 * @author Богдан Рабыков
 * @version 1.0
 */
public class CRUD {

    /**
     * Универсальный метод, реализующий операцию добавления строки в таблицу в базе данных.
     * В методе реализована SQL транзакция, при возникновении ошибки, все изменения откатываются.
     *
     * @param connection - соединение с базой данных
     * @param data       - данные, которые необходимо добавить в таблицу
     * @param tableName  - имя таблицы, в которую необходимо добавить данные
     * @param colNames   - имена столбцов в таблице
     * @throws SQLException
     */
    public static void insertRow(Connection connection, ArrayList<String> data, String tableName, ArrayList<String> colNames) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();

            String command = "BEGIN; INSERT INTO cleverbank." + tableName + " ( ";
            for (int i = 0; i < colNames.size(); i++) {
                if (colNames.size() - 1 != i)
                    command = command + " " + colNames.get(i) + ", ";
                else
                    command = command + " " + colNames.get(i) + " ";
            }

            command = command + " ) VALUES ( ";

            for (int i = 0; i < data.size() && data.get(i) != ""; i++) {
                if (tryParseInt(data.get(i)) || tryParseDouble(data.get(i))) {
                    if (data.size() - 1 == i) {
                        command = command + " " + data.get(i) + " )";
                    } else {
                        command = command + " " + data.get(i) + ", ";
                    }
                } else {
                    if (data.size() - 1 == i) {
                        command = command + " \'" + data.get(i) + "\' )";
                    } else {
                        command = command + " \'" + data.get(i) + "\', ";
                    }
                }
            }
            command = command + "; COMMIT;";
            statement.executeUpdate(command);
            System.out.println("Row has been added to the table");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Insert operation failed");

            statement.execute("ROLLBACK; ");
        } finally {
            statement.close();
        }
    }

    /**
     * Универсальный метод, реализующий операцию обновления строки таблицы в базе данных.
     * В методе реализована SQL транзакция, при возникновении ошибки, все изменения откатываются.
     *
     * @param connection - соединение с базой данных
     * @param data       - данные, которые необходимо внести
     * @param tableName  - имя таблицы
     * @param colNames   - имена столбцов
     * @throws SQLException
     */
    public static void updateRow(Connection connection, ArrayList<String> data, String tableName, ArrayList<String> colNames) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();

            String command = "BEGIN; UPDATE cleverbank." + tableName + " SET ";
            for (int i = 0; i < data.size() && data.get(i) != ""; i++) {
                if (tryParseInt(data.get(i)) || (tryParseDouble(data.get(i)))) {
                    if (data.size() - 1 == i) {
                        command = command + colNames.get(i) + " = " + data.get(i) + " ";
                        command = command + " WHERE " + colNames.get(0) + " = " + data.get(0);

                        command = command + "; COMMIT;";
                        statement.executeUpdate(command);
                    } else {
                        if (i != 0)
                            command = command + colNames.get(i) + " = " + data.get(i) + ", ";
                    }
                } else {
                    if (data.size() - 1 == i) {
                        command = command + colNames.get(i) + " = \'" + data.get(i) + "\' ";
                        command = command + " WHERE " + colNames.get(0) + " = " + data.get(0);

                        command = command + "; COMMIT;";
                        statement.executeUpdate(command);
                    } else {
                        if (i != 0)
                            command = command + colNames.get(i) + " = \'" + data.get(i) + "\', ";
                    }
                }
            }

            System.out.println("Row has been updated in the table");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Update operation failed");
            statement.executeUpdate("ROLLBACK; ");
        } finally {
            statement.close();
        }
    }

    /**
     * Mетод, реализующий операцию удаление строки из таблицы в базе данных.
     * В методе реализована SQL транзакция, при возникновении ошибки, все изменения откатываются.
     *
     * @param connection - соединение с базой данных
     * @param tableName  - название таблицы
     * @param id         - идентификатор строки, которую необходимо удалить
     * @throws SQLException
     */
    public static void deleteRow(Connection connection, String tableName, int id) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("BEGIN; DELETE FROM cleverbank." + tableName + " WHERE id =" + id + "; COMMIT ;");
            System.out.println("Row has been deleted from the table");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Delete operation failed");
            statement.executeUpdate("ROLLBACK;");
        } finally {
            statement.close();
        }
    }

    /**
     * Метод, реализующий выбор строк из таблицы в базе данных по определённому полю и значению
     *
     * @param connection - соединение с базой данных
     * @param tableName  - название таблицы
     * @param field      - поле по которому происходит выбор
     * @param value      - значение по которому происходит выбор
     * @throws SQLException
     */
    public static void selectRow(Connection connection, String tableName, String field, String value) throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            String typeSearch = "SELECT data_type FROM information_schema.columns WHERE table_name = '" + tableName + "' AND column_name = '" + field + "';";
            preparedStatement = connection.prepareStatement(typeSearch);

            // Выполняем запрос и получаем результат
            ResultSet resultSet = preparedStatement.executeQuery();

            // Получаем метаданные результата
            resultSet.next();
            Object columnValue = resultSet.getObject(1);
            String newtype = columnValue.toString();
            preparedStatement.close();

            String command = " SELECT * FROM cleverbank." + tableName + " WHERE " + field + " = '" + value + "'::" + newtype + "; ";

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(command);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++)
                System.out.print(metaData.getColumnName(i) + "\t");

            System.out.println();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++)
                    System.out.print(resultSet.getString(i) + "\t");
                System.out.println();
            }

            // Закрываем ресурсы
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Select operation failed");
        }
    }

    /**
     * Метод, реализующий отображение таблицы
     *
     * @param connection - соединение с базой данных
     * @param tableName  - название таблицы
     * @throws SQLException
     */
    public static void showTable(Connection connection, String tableName) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cleverbank." + tableName + ";");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++)
                System.out.print(metaData.getColumnName(i) + "\t");

            System.out.println();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++)
                    System.out.print(resultSet.getString(i) + "\t");
                System.out.println();
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("The table could not be output");
        }
    }

    /**
     * Метод, получающий название столбцов в таблице
     *
     * @param connection - соединение с базой данных
     * @param tableName  - название таблицы
     * @return
     */
    public static ArrayList<String> getColNames(Connection connection, String tableName) {
        Statement statement = null;
        ArrayList<String> colNames = null;
        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT column_name FROM information_schema.columns WHERE table_name = '" + tableName + "';");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            colNames = new ArrayList<String>(columnCount);
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++)
                    colNames.add(resultSet.getString(i));
            }
            resultSet.close();
            statement.close();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get column names");
        }
        return colNames;
    }

    /**
     * Метод, проверяющий является ли строка числом типа Integer
     *
     * @param str - исходная строка
     * @return - в случае если строка является число возвращает True, иначе False
     */
    private static boolean tryParseInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Метод, проверяющий является ли строка числом типа Double
     *
     * @param str - исходная строка
     * @return - в случае если строка является число возвращает True, иначе False
     */
    private static boolean tryParseDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
