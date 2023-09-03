package com.CleverBank;

import JDBC.SQLFileExecuter;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import static PDFWorking.AccountStatementGenerator.generateAccountStatement;
import static PDFWorking.CheckGenerator.*;
import static DataOperations.CRUD.*;
import static DataOperations.ScoreOperations.*;
import static java.sql.DriverManager.getConnection;

/**
 * Главный класс приложение CleverBank
 *
 * @author Богдан Рыбаков
 * @version 1.0
 */
public class CleverBankApp {
    //Адрес для подключения к PostgreSQL
    static final String DB_URL = "jdbc:postgresql://localhost:5432/";

    static Scanner in = new Scanner(System.in);

    /**
     * Точка входа в программу
     *
     * @param argv
     */
    public static void main(String[] argv) {

        //Проверка наличия драйвера, для подключения и управления базами данных PostgreSQL
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        //Имя пользователя PostgreSQL
        System.out.println("Enter PostgreSQL Username:");
        final String USER = in.nextLine();

        //Пароль
        System.out.println("Enter password:");
        final String PASS = in.nextLine();

        try {
            //Создание подключения к базе данных
            Connection connection = getConnection(DB_URL, USER, PASS);

            System.out.println("You are connected to the database");

            System.out.print("Generate a database? Y/N\n");
            String c = in.nextLine();

            //Запрос на создание базы данных. Запускается скрипт из файла
            try {
                if (c.toLowerCase().equals("y")) {
                    String path = new File("src/main/sql/createdb.sql").getAbsolutePath();
                    SQLFileExecuter.sqlFileExecuter(path, connection);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.print("Fill the database with information? Y/N\n");
            c = in.nextLine();

            //Запрос на заполнение базы данных нформацией. Запускается скрипт из файла
            try {
                if (c.toLowerCase().equals("y")) {
                    String path = new File("src/main/sql/filldb.sql").getAbsolutePath();
                    SQLFileExecuter.sqlFileExecuter(path, connection);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            int point;

            //Главный цикл программы
            loop:
            while (true) {
                printMenu();

                //Выбор пункта меню
                try {
                    String p = in.next();
                    point = Integer.parseInt(p);
                } catch (Exception e) {
                    e.getMessage();
                    point = 0;
                }

                switch (point) {
                    case 1:
                        //Переход к меню операций со счетами
                        scoreMenu(connection);
                        break;
                    case 2:
                        //Переход к меню операций с таблицами
                        tableMenu(connection);
                        break;

                    case 3:
                        //Получение выписок по счетам пользователей
                        System.out.println("Enter the id of the user whose statement you want to receive:");
                        try {
                            String id = in.next();
                            int user_id = Integer.parseInt(id);

                            System.out.println("Select the period for which you want to receive a statement");
                            System.out.println("1. Last month");
                            System.out.println("2. Last year");
                            System.out.println("3. All time");

                            String per = in.next();
                            int period = Integer.parseInt(per);
                            LocalDateTime startPeriod, endPeriod = LocalDateTime.now();
                            //Выбор периода для получения выписки
                            switch (period) {
                                case 1:
                                    startPeriod = endPeriod.minusMonths(1);
                                    break;
                                case 2:
                                    startPeriod = endPeriod.minusYears(1);
                                    break;
                                default:
                                    startPeriod = endPeriod.minusYears(20);
                            }

                            generateAccountStatement(connection, user_id, startPeriod, endPeriod);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        break loop;
                    default:
                        System.out.println("Invalid point");
                }
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод, запускающий меню работы с таблицами
     *
     * @param connection - соединение с базой данных
     */

    public static void tableMenu(Connection connection) {
        int point;
        String tableName, field, value;

        loop:
        while (true) {
            //Вывод пунктов меню
            printTableMenu();

            //Считывание выбранного пункта
            try {
                String p = in.next();
                point = Integer.parseInt(p);
            } catch (Exception e) {
                e.getMessage();
                point = 0;
            }

            switch (point) {
                //Вывод таблицы
                case 1:
                    try {
                        printTableList();
                        System.out.print("Enter the name of the table you want to view:");
                        tableName = in.next();
                        showTable(connection, tableName.toLowerCase());
                    } catch (SQLException e) {
                        System.out.println("The table could not be output");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                //Вывод записи из таблицы
                case 2:
                    try {
                        printTableList();
                        System.out.print("Enter the name of the table from which you want to get an entry:");
                        tableName = in.next();
                        System.out.print("Enter the name of the field by which the selection will take place:");
                        field = in.next();
                        System.out.print("Enter the value by which the selection will take place:");
                        value = in.next();
                        selectRow(connection, tableName.toLowerCase(), field.toLowerCase(), value);
                    } catch (SQLException e) {
                        System.out.println("The table could not be output");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                //Добавление записи в таблицу
                case 3:
                    try {
                        printTableList();
                        System.out.print("Enter the name of the table you want to add the entry to:");
                        tableName = in.next();
                        ArrayList<String> colNames = getColNames(connection, tableName.toLowerCase());
                        ArrayList<String> inputValues = new ArrayList<String>();
                        colNames.remove(0);
                        for (int i = 0; i < colNames.size(); i++) {
                            System.out.print("Enter a value for the field " + colNames.get(i) + ":");
                            inputValues.add(in.next());
                        }
                        insertRow(connection, inputValues, tableName.toLowerCase(), colNames);

                    } catch (SQLException e) {
                        System.out.println("Failed to add row to table");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                //Удаление записи из таблицы
                case 4:
                    try {
                        int id;
                        printTableList();
                        System.out.print("Enter the name of the table you want to delete an entry to:");
                        tableName = in.next();
                        System.out.print("Enter the ID of the record you want to delete:");
                        id = in.nextInt();

                        deleteRow(connection, tableName.toLowerCase(), id);
                    } catch (SQLException e) {
                        System.out.println("Failed to delete row from table");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                //Изменение записи в таблице
                case 5:
                    try {
                        printTableList();
                        System.out.print("Enter the name of the table you want to change the row for:");
                        tableName = in.next();
                        ArrayList<String> colNames = getColNames(connection, tableName.toLowerCase());
                        ArrayList<String> inputValues = new ArrayList<String>();
                        System.out.print("Enter the ID of the row you want to change:");
                        String id = in.next();
                        inputValues.add(0, id);

                        for (int i = 1; i < colNames.size(); i++) {
                            System.out.print("Enter a value for the field " + colNames.get(i) + ":");
                            inputValues.add(in.next());
                        }
                        updateRow(connection, inputValues, tableName.toLowerCase(), colNames);

                    } catch (SQLException e) {
                        System.out.println("Failed to add row to table");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                //Прекращение работы меню таблиц
                case 6:
                    break loop;
                default:
                    System.out.println("Invalid point");
            }
        }
    }

    /**
     * Метод, запускающий меню работы со счетами
     *
     * @param connection - соединение с базой данных
     */
    public static void scoreMenu(Connection connection) {
        int point;
        double deposit;
        String scoreNumber, outputScore;
        LocalDateTime localDateTime;

        loop:
        while (true) {
            //Вывод пунктов меню
            printScoreOperationsMenu();

            //Счтывание выбранного пункта
            try {
                String p = in.next();
                point = Integer.parseInt(p);
            } catch (Exception e) {
                e.getMessage();
                point = 0;
            }

            switch (point) {
                //Пополнение счёта
                case 1:
                    try {
                        System.out.print("Enter score number:");
                        scoreNumber = in.next();
                        System.out.print("Enter deposit amount:");
                        try {
                            String d = in.next();
                            deposit = Double.parseDouble(d);
                        } catch (Exception e) {
                            e.getMessage();
                            break;
                        }
                        if (deposit <= 0) {
                            System.out.println("Invalid deposit value");
                            break;
                        }

                        boolean res = addingFunds(connection, scoreNumber, deposit);
                        if (res) {
                            ArrayList<String> data = new ArrayList<String>();
                            ArrayList<String> colNames = getColNames(connection, "transactions");
                            colNames.remove(0);

                            localDateTime = LocalDateTime.now();

                            data.add(Double.toString(deposit));
                            data.add("" + localDateTime.getYear() + "-" + "" + localDateTime.getMonth() +
                                    "-" + localDateTime.getDayOfMonth() + " " + localDateTime.getHour() +
                                    ":" + localDateTime.getMinute() + ":" + localDateTime.getSecond());
                            data.add("DEPOSIT");
                            data.add(scoreNumber);

                            //Добавление записи в таблицу с транзакциями
                            insertRow(connection, data, "transactions", colNames);
                            //Создание чека
                            generateDepositCheck(connection, scoreNumber, deposit);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                //Снятие средств со счёта
                case 2:
                    try {
                        System.out.print("Enter score number:");
                        scoreNumber = in.next();

                        System.out.print("Enter debit amount:");
                        try {
                            String d = in.next();
                            deposit = Double.parseDouble(d);
                        } catch (Exception e) {
                            e.getMessage();
                            break;
                        }

                        if (deposit <= 0) {
                            System.out.println("Invalid deposit value");
                            break;
                        }
                        //Проверка на то, хвататет ли средств для списания
                        if (deposit > getBalance(connection, scoreNumber)) {
                            System.out.println("Amount being debited exceeds the score balance");
                            break;
                        } else {
                            //Снятие средств
                            addingFunds(connection, scoreNumber, deposit * (-1));

                            ArrayList<String> data = new ArrayList<String>();
                            ArrayList<String> colNames = getColNames(connection, "transactions");
                            colNames.remove(0);

                            localDateTime = LocalDateTime.now();

                            data.add(Double.toString(deposit));
                            data.add("" + localDateTime.getYear() + "-" + "" + localDateTime.getMonth() +
                                    "-" + localDateTime.getDayOfMonth() + " " + localDateTime.getHour() +
                                    ":" + localDateTime.getSecond());
                            data.add(scoreNumber);
                            data.add("DEBITING");
                            //Добавление записи в таблицу с транзакциями
                            insertRow(connection, data, "transactions", colNames);
                            //Создание чека
                            generateDebitingCheck(connection, scoreNumber, deposit);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                //Перевод со счёта на счёт
                case 3:
                    try {
                        System.out.print("Enter input score number:");
                        scoreNumber = in.next();

                        System.out.print("Enter output score number:");
                        outputScore = in.next();

                        System.out.print("Enter deposit amount:");
                        try {
                            String d = in.next();
                            deposit = Double.parseDouble(d);
                        } catch (Exception e) {
                            e.getMessage();
                            break;
                        }
                        if (deposit <= 0) break;
                        //Выполнение операции перевода
                        boolean res = trasfer(connection, scoreNumber, outputScore, deposit);
                        //Создание чека
                        if (res)
                            generateTransactionCheck(connection, scoreNumber, outputScore, deposit);
                    } catch (SQLException e) {
                        System.out.println("Transfer operation failed");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                    break;
                //Прекращение работы меню операций со счетами
                case 4:
                    break loop;
                default:
                    System.out.println("Invalid point");
            }
        }
    }

    /**
     * Метод, выводящий список пунтов с операциями со счётом
     */
    public static void printScoreOperationsMenu() {
        System.out.println("\n------Score operations menu------");
        System.out.println("1. Deposit funds to the account");
        System.out.println("2. Withdraw funds from the account");
        System.out.println("3. Transfer from score to score");
        System.out.println("4. Exit\n");
    }

    /**
     * Метод, выводящий главное меню программы
     */
    public static void printMenu() {
        System.out.println("\n---------Menu---------");
        System.out.println("1. Score operations");
        System.out.println("2. Tables operations");
        System.out.println("3. Account statement");
        System.out.println("4. Exit");
        System.out.println();
    }

    /**
     * Метод, выводящий список таблиц
     */
    public static void printTableList() {
        System.out.println("\n-----Tables list-----");
        System.out.println("Banks");
        System.out.println("Users");
        System.out.println("Scores");
        System.out.println("Transactions\n");
    }

    /**
     * Метод, выводящий список пунктов операций с таблцами
     */
    public static void printTableMenu() {
        System.out.println("\n---------Tables---------");
        System.out.println("1. Show tables");
        System.out.println("2. Select an entry from the table");
        System.out.println("3. Add an entry to the table");
        System.out.println("4. Delete an entry from the table");
        System.out.println("5. Edit an entry in the table");
        System.out.println("6. Exit\n");
    }
}
