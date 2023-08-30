package DataOperations;

import java.sql.*;
import java.util.ArrayList;

public class CRUD {
    public static void insertRow(Connection connection, ArrayList<String> data, String tableName, ArrayList<String> colNames) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();

            String command = "BEGIN; INSERT INTO " + tableName + " ( ";
            for (int i = 1; i < colNames.size(); i++) {
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
        } catch (SQLException e) {
            System.out.println("Insert operation failed");
            statement.execute("ROLLBACK; ");
        } finally {
            statement.close();
        }
    }

    public static void updateRow(Connection connection, ArrayList<String> data, String tableName, ArrayList<String> colNames) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();

            String command = "BEGIN; UPDATE " + tableName + " SET ";
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


        } catch (SQLException e) {
            System.out.println("Update operation failed");
            statement.executeUpdate("ROLLBACK; ");
        } finally {
            statement.close();
        }
    }

    public static void deleteRow(Connection connection, String tableName, int id) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("BEGIN; DELETE * FROM cleverbank." + tableName + " WHERE id =" + id + "; COMMIT ;");
        } catch (SQLException e) {
            System.out.println("Delete operation failed");
            statement.executeUpdate("ROLLBACK;");

        } finally {
            statement.close();
        }
    }

    public static void showTable(Connection connection, String tableName) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cleverbank." + tableName + ";");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("The table could not be output");
        }
    }

    private static boolean tryParseInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean tryParseDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
