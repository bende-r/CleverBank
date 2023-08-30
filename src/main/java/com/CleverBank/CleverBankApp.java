package com.CleverBank;

import DataOperations.ScoreOperations;
import JDBC.SQLFileReader;

import java.sql.*;
import java.util.Scanner;

import static DataOperations.CRUD.showTable;
import static java.sql.DriverManager.getConnection;

public class CleverBankApp {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/";
    static final String USER = "postgres";
    static final String PASS = "postgres";
    static Scanner in = new Scanner(System.in);

    public static void main(String[] argv) {
        SQLFileReader sqlFileReader = new SQLFileReader();


        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

      /*  System.out.println("Enter PostgreSQL Username:");
        final String USER = in.nextLine();

        System.out.println("Enter password:");
        final String PASS = in.nextLine();
*/
        try {
            Connection connection = getConnection(DB_URL, USER, PASS);


            System.out.println("You are connected to the database");

          /*  System.out.print("Generate a database? Y/N\n");
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
*/

            String scoreNumber;
            int deposit;
            while (true) {
                printMenu();
                int point = in.nextInt();

                switch (point) {
                    case 1:
                        try {
                            System.out.print("Enter score number:");
                            scoreNumber = in.next();

                            System.out.print("Enter deposit count:");
                            deposit = in.nextInt();

                            ScoreOperations.addingFunds(connection, scoreNumber, deposit);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                        break;
                    case 2:
                        tableMenu(connection);

                        break;
                    case 3:

                        connection.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid point");

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void tableMenu(Connection connection) {
        int point;
        String tableName;
        loop:
        while (true) {
            printTableMenu();
            point = in.nextInt();

            switch (point) {
                case 1:
                    try {
                        System.out.println("-----Tables list-----");
                        System.out.println("Banks");
                        System.out.println("Users");
                        System.out.println("Scores");
                        System.out.print("Enter the name of the table you want to view:");
                        tableName = in.next();
                        showTable(connection, tableName.toLowerCase());
                    } catch (SQLException e) {
                        System.out.println("The table could not be output");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;

                case 5:
                    break;
                case 6:
                    break loop;
                default:
                    System.out.println("Invalid point");
            }

        }

    }


    public static void printMenu() {
        System.out.println("---------Menu---------");
        System.out.println("1. Score operations");
        System.out.println("2. Tables operations");
        System.out.println("3. Exit");
    }

    public static void printTableMenu() {
        System.out.println("---------Tables---------");
        System.out.println("1. Show tables");
        System.out.println("2. Select an entry from the table");
        System.out.println("3. Add an entry to the table");
        System.out.println("4. Delete an entry from the table");
        System.out.println("5. Edit an entry in the table");
        System.out.println("6. Exit");
    }

}
