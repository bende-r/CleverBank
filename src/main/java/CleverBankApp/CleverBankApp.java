package CleverBankApp;

import JDBC.SQLFileExecuter;
import JDBC.SQLFileReader;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class CleverBankApp {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/";
    static final String USER = "postgres";
    static final String PASS = "postgres";

    public static void main(String[] argv) {
        SQLFileReader sqlFileReader = new SQLFileReader();
        Scanner in = new Scanner(System.in);

        System.out.println("Testing connection to PostgreSQL JDBC");

        Class<?> clazz = CleverBankApp.class;
        String className = clazz.getName();
        System.out.println("Полное имя класса: " + className);


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement statement = connection.createStatement();

            System.out.print("Generate a database? Y/N\n");
            String c = in.nextLine();

            try {
                if (c.toLowerCase().equals("y")) {
                    String path = new File("src/main/sql/createdb.sql").getAbsolutePath();
                    SQLFileExecuter.SQLFileExecuter(path, statement);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.print("Fill the database with information? Y/N\n");
            c = in.nextLine();

            try {
                if (c.toLowerCase().equals("y")) {
                    String path = new File("src/main/sql/filldb.sql").getAbsolutePath();
                    SQLFileExecuter.SQLFileExecuter(path, statement);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            statement.close();
            connection.close();

            System.out.println("You are connected to the database");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
