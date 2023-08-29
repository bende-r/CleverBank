package com.CleverBank;

import DataOperations.Operations;
import JDBC.SQLFileExecuter;
import JDBC.SQLFileReader;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

import static java.sql.DriverManager.getConnection;

public class CleverBankApp {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/";
    //static final String USER = "postgres";
    // static final String PASS = "postgres";

    public static void main(String[] argv) {
        SQLFileReader sqlFileReader = new SQLFileReader();
        Scanner in = new Scanner(System.in);

        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        System.out.println("Enter PostrgeSQL Username:");
        final String USER = in.nextLine();

        System.out.println("Enter password:");
        final String PASS = in.nextLine();

        try {
            Connection connection = getConnection(DB_URL, USER, PASS);
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

            System.out.println("You are connected to the database");
            while (true) {
                printMenu();
                int point = in.nextInt();

                switch (point) {
                    case 1:
                        Operations.addingFunds(statement, "4819777576", 100000);
                        break;
                    case 2:
                        break;
                    case 3:
                        statement.close();
                        connection.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid item");
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void printMenu() {
        System.out.println("---------Menu---------");
        System.out.println("1. Score operations");
        System.out.println("2. Tables operations");
        System.out.println("3. Exit");
    }

}
