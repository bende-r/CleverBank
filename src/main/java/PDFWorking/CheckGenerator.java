package PDFWorking;

import java.security.SecureRandom;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static PDFWorking.PDFGenerator.generatePDF;


/**
 * Класс для создания разных видов чеков
 *
 * @author Богдан Рыбаков
 * @version 1.0
 */
public class CheckGenerator {

    /**
     * Метод для создания чека о переводе со счёта на счёт.
     * Внутри себя обращается к методу, создающему PDF документ {@link PDFGenerator#generatePDF(ArrayList, String, String)}
     *
     * @param connection  - соединение с базой данных
     * @param inputScore  - счёт получаетля
     * @param outputScore - счёт отправителя
     * @param deposit     - сумма перевода
     */

    public static void generateTransactionCheck(Connection connection, String inputScore, String outputScore, double deposit) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        StringBuilder senderBank = null;
        StringBuilder recipientBank = null;
        StringBuilder depos = new StringBuilder(decimalFormat.format(deposit));
        StringBuilder checkNumber = new StringBuilder();

        //Генерируется номер чека
        long seed = System.currentTimeMillis();
        LocalDateTime localDateTime = LocalDateTime.now();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed);
        long randomTenDigitNumber = secureRandom.nextLong() % 10000000000L;
        if (randomTenDigitNumber < 0)
            randomTenDigitNumber = -randomTenDigitNumber;
        checkNumber.append(randomTenDigitNumber);

        Statement statement = null;
        try {
            //Команда для получения названия банка получателя
            String commandRecipient = " SELECT cleverbank.banks.bank_name FROM cleverbank.banks, cleverbank.scores WHERE cleverbank.scores.bank_id = banks.id AND cleverbank.scores.score_number = '" + inputScore + "'; ";

            //Команда для получения названия банка отправителя
            String commandSender = " SELECT cleverbank.banks.bank_name FROM cleverbank.banks, cleverbank.scores WHERE cleverbank.scores.bank_id = banks.id AND cleverbank.scores.score_number = '" + outputScore + "'; ";

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(commandRecipient);
            resultSet.next();
            recipientBank = new StringBuilder(resultSet.getString(1));

            resultSet = statement.executeQuery(commandSender);
            resultSet.next();
            senderBank = new StringBuilder(resultSet.getString(1));


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while (senderBank.length() < 32)
            senderBank.insert(0, ' ');

        while (recipientBank.length() < 29)
            recipientBank.insert(0, ' ');

        while (depos.length() < 42)
            depos.insert(0, ' ');

        while (checkNumber.length() < 40)
            checkNumber.insert(0, ' ');

        //Создание текста чека
        ArrayList<String> text = new ArrayList<String>();
        text.add("--------------------------------------------------");
        text.add("|                     Bank check                 |");
        text.add("| Check:" + checkNumber + " |");
        text.add("| " + localDateTime.getDayOfMonth() + "-" + localDateTime.getMonthValue() +
                "-" + localDateTime.getYear() + "                              "
                + localDateTime.getHour() + ":" + localDateTime.getMinute() + ":" + localDateTime.getSecond() + " |");
        text.add("| Operation type:                    Transaction |");
        text.add("| Sender's bank:" + senderBank.toString() + " |");
        text.add("| Recipient's bank:" + recipientBank.toString() + " |");
        text.add("| Output score:                       " + outputScore + " |");
        text.add("| Input score:                        " + inputScore + " |");
        text.add("| Sum:" + depos + " |");
        text.add("--------------------------------------------------");

        //Обращение к методу, создающему PDF документ
        generatePDF(text, "checks", localDateTime.getDayOfMonth() + "-" + localDateTime.getMonthValue() +
                "-" + localDateTime.getYear() + "-" + localDateTime.getHour() + "-" + localDateTime.getMinute() + "-"
                + localDateTime.getSecond());
    }

    /**
     * Метод, создающий чек для операции пополнения счёта.
     * Внутри себя обращается к методу, создающему PDF документ {@link PDFGenerator#generatePDF(ArrayList, String, String)}
     *
     * @param connection - соединение с базой данных
     * @param inputScore - счёт получаетля
     * @param deposit    - сумма пополнения
     */
    public static void generateDepositCheck(Connection connection, String inputScore, double deposit) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        StringBuilder recipientBank = null;
        StringBuilder depos = new StringBuilder(decimalFormat.format(deposit));
        StringBuilder checkNumber = new StringBuilder();

        //Генерируется номер чека
        long seed = System.currentTimeMillis();
        LocalDateTime localDateTime = LocalDateTime.now();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed);
        long randomTenDigitNumber = secureRandom.nextLong() % 10000000000L;
        if (randomTenDigitNumber < 0)
            randomTenDigitNumber = -randomTenDigitNumber;
        checkNumber.append(randomTenDigitNumber);

        Statement statement = null;
        try {
            //Команда для получения названия банка получателя
            String commandRecipient = " SELECT cleverbank.banks.bank_name FROM cleverbank.banks, cleverbank.scores WHERE cleverbank.scores.bank_id = banks.id AND cleverbank.scores.score_number = '" + inputScore + "'; ";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(commandRecipient);
            resultSet.next();
            recipientBank = new StringBuilder(resultSet.getString(1));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while (recipientBank.length() < 29)
            recipientBank.insert(0, ' ');

        while (depos.length() < 42)
            depos.insert(0, ' ');

        while (checkNumber.length() < 40)
            checkNumber.insert(0, ' ');

        //Создание текста чека
        ArrayList<String> text = new ArrayList<String>();
        text.add("--------------------------------------------------");
        text.add("|                     Bank check                 |");
        text.add("| Check:" + checkNumber + " |");
        text.add("| " + localDateTime.getDayOfMonth() + "-" + localDateTime.getMonthValue() +
                "-" + localDateTime.getYear() + "                              "
                + localDateTime.getHour() + ":" + localDateTime.getMinute() + ":" + localDateTime.getSecond() + " |");
        text.add("| Operation type:                        Deposit |");
        text.add("| Recipient's bank:" + recipientBank.toString() + " |");
        text.add("| Input score:                        " + inputScore + " |");
        text.add("| Sum:" + depos + " |");
        text.add("--------------------------------------------------");

        //Обращение к методу, создающему PDF документ
        generatePDF(text, "checks", localDateTime.getDayOfMonth() + "-" + localDateTime.getMonthValue() +
                "-" + localDateTime.getYear() + "-" + localDateTime.getHour() + "-" + localDateTime.getMinute() + "-"
                + localDateTime.getSecond());
    }

    /**
     * Метод, создающий чек для операции списания средств со счёта
     * Внутри себя обращается к методу, создающему PDF документ {@link PDFGenerator#generatePDF(ArrayList, String, String)}
     *
     * @param connection  - соединение с базой данных
     * @param outputScore - счёт, с которого снимаются средства
     * @param debit       - списываемая сумма
     */
    public static void generateDebitingCheck(Connection connection, String outputScore, double debit) {
        StringBuilder recipientBank = null;
        StringBuilder debet = new StringBuilder(String.valueOf(debit));
        StringBuilder checkNumber = new StringBuilder();

        //Генерация номера чека
        long seed = System.currentTimeMillis();
        LocalDateTime localDateTime = LocalDateTime.now();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed);
        long randomTenDigitNumber = secureRandom.nextLong() % 10000000000L;
        if (randomTenDigitNumber < 0)
            randomTenDigitNumber = -randomTenDigitNumber;
        checkNumber.append(randomTenDigitNumber);

        Statement statement = null;

        try {
            //Команда для получения названия банка
            String commandSender = " SELECT cleverbank.banks.bank_name FROM cleverbank.banks, cleverbank.scores WHERE cleverbank.scores.bank_id = banks.id AND cleverbank.scores.score_number = '" + outputScore + "'; ";

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(commandSender);
            resultSet.next();
            recipientBank = new StringBuilder(resultSet.getString(1));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while (recipientBank.length() < 29)
            recipientBank.insert(0, ' ');

        while (debet.length() < 42)
            debet.insert(0, ' ');

        while (checkNumber.length() < 40)
            checkNumber.insert(0, ' ');

        //Текст чека
        ArrayList<String> text = new ArrayList<String>();
        text.add("--------------------------------------------------");
        text.add("|                     Bank check                 |");
        text.add("| Check:" + checkNumber + " |");
        text.add("| " + localDateTime.getDayOfMonth() + "-" + localDateTime.getMonthValue() +
                "-" + localDateTime.getYear() + "                              "
                + localDateTime.getHour() + ":" + localDateTime.getMinute() + ":" + localDateTime.getSecond() + " |");
        text.add("| Operation type:                       Debiting |");
        text.add("| Sender`s bank:   " + recipientBank.toString() + " |");
        text.add("| Output score:                       " + outputScore + " |");
        text.add("| Sum:" + debet + " |");
        text.add("--------------------------------------------------");

        //Вызов метода, создающего PDF документ
        generatePDF(text, "checks", localDateTime.getDayOfMonth() + "-" + localDateTime.getMonthValue() +
                "-" + localDateTime.getYear() + "-" + localDateTime.getHour() + "-" + localDateTime.getMinute() + "-"
                + localDateTime.getSecond());
    }


}
