package PDFWorking;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static PDFWorking.PDFGenerator.generatePDF;

/**
 * Класс для создание выписок со счёта.
 *
 * @author Богдан Рыбаков
 * @version 1.0
 */
public class AccountStatementGenerator {
    /**
     * Функция для создания текста выписки. Внутри себя обращается к методу, создающему PDF документ {@link PDFGenerator#generatePDF(ArrayList, String, String)}
     *
     * @param connection    - соединение с базой данных
     * @param user_id       - идентификатор пользователя, для чьих счетов нужно делать выписки
     * @param startOfPeriod - начало периода, по которому проводится выписка
     * @param endOfPeriod   - конец периода, по которому проводится выписка
     */
    public static void generateAccountStatement(Connection connection, int user_id, LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {

        StringBuilder checkNumber = new StringBuilder(), FIO = new StringBuilder();
        ArrayList<ArrayList<String>> scoreNumbers = new ArrayList<>();
        ArrayList<ArrayList<String>> transactions = new ArrayList<>();
        StringBuilder fileName = new StringBuilder();
        LocalDateTime localDateTime = LocalDateTime.now();

        Statement statement = null;

        try {
            //SQL-запрос для получения ФИО клиента банка
            String commandUserFIO = " SELECT * FROM cleverbank.users WHERE cleverbank.users.id = " + user_id + "; ";

            //SQL-запрос для получения спска счетов пользователя
            String commandAllScoreNumbers = "SELECT cleverbank.scores.score_number, cleverbank.scores.balance, cleverbank.scores.currency FROM cleverbank.users, cleverbank.scores WHERE cleverbank.scores.user_id = " + user_id + " AND users.id = " + user_id + ";";

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(commandUserFIO);
            resultSet.next();

            FIO.append(resultSet.getString(2) + " ");
            FIO.append(resultSet.getString(3) + " ");
            FIO.append(resultSet.getString(4));


            fileName.append(resultSet.getString(2) + "_");
            fileName.append(resultSet.getString(3) + "_");
            fileName.append(resultSet.getString(4) + "_");

            resultSet = statement.executeQuery(commandAllScoreNumbers);
            while (resultSet.next()) {
                ArrayList<String> res = new ArrayList<>();
                res.add(resultSet.getString(1));
                res.add(resultSet.getString(2));
                res.add(resultSet.getString(3));
                scoreNumbers.add(res);
            }
            resultSet.close();

            for (ArrayList<String> number : scoreNumbers) {
                ArrayList<String> arr = new ArrayList<>();

                //Запрос для получения списка транзакций по счетам пользователя
                String command = "SELECT sender_score_number,recipient_score_number, transfer_amount, date " +
                        "FROM cleverbank.transactions WHERE transactions.sender_score_number = '" + number.get(0) + "' " +
                        "OR transactions.recipient_score_number ='" + number.get(0) + "' " +
                        "AND transactions.date>='" + startOfPeriod.getYear() + "-" + startOfPeriod.getMonthValue() + "-"
                        + startOfPeriod.getDayOfMonth() + "' AND transactions.date <='" + endOfPeriod.getYear() + "-"
                        + endOfPeriod.getMonthValue() + "-" + endOfPeriod.getDayOfMonth() + "';";
                resultSet = statement.executeQuery(command);

                while (resultSet.next()) {
                    for (int i = 1; i <= 4; i++) {
                        System.out.print(resultSet.getString(i) + "\t");
                        arr.add(resultSet.getString(i));
                    }
                    System.out.println();
                    transactions.add(arr);
                }
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while (FIO.length() < 58)
            FIO.insert(0, ' ');

        while (checkNumber.length() < 40)
            checkNumber.insert(0, ' ');

        //Создание текста выписки
        for (ArrayList<String> scoreNumber : scoreNumbers) {
            ArrayList<String> text = new ArrayList<String>();
            text.add("---------------------------------------------------------------------");
            text.add("|                           Account Statement                       |");
            text.add("| Client:" + FIO + " |");
            text.add("| Score number:                                          " + scoreNumber.get(0) + " |");

            StringBuilder period = new StringBuilder();
            period.append(startOfPeriod.getDayOfMonth()).append(".").append(startOfPeriod.getMonthValue()).append(".").append(startOfPeriod.getYear()).append(" - ").append(endOfPeriod.getDayOfMonth()).append(".").append(endOfPeriod.getMonthValue()).append(".").append(endOfPeriod.getYear());

            while (period.length() < 57)
                period.insert(0, ' ');

            text.add("| Period: " + period + " |");

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String bal = scoreNumber.get(1).replace("$", "").replace(",", "");
            float b = Float.parseFloat(bal);
            StringBuilder balance = new StringBuilder(decimalFormat.format(b));

            while (balance.length() < 55)
                balance.insert(0, ' ');

            StringBuilder currency = new StringBuilder(scoreNumber.get(2));
            while (currency.length() < 55)
                currency.insert(0, ' ');
            text.add("| Currency: " + currency + " |");
            text.add("| Balance:  " + balance + " |");

            StringBuilder date = new StringBuilder();
            date.append(localDateTime.getDayOfMonth()).append(".").append(localDateTime.getMonthValue()).append(".").append(localDateTime.getYear()).append(" ").append(localDateTime.getHour()).append(":").append(localDateTime.getMinute()).append(":").append(localDateTime.getSecond());
            while (date.length() < 37)
                date.insert(0, ' ');

            text.add("| Date of statement creation: " + date + " |");
            text.add("---------------------------------------------------------------------");
            text.add("|        Date         |          Note           |       Amount      ");
            text.add("---------------------------------------------------------------------");
            for (ArrayList<String> tr : transactions) {
                if (tr.get(0).equals(scoreNumber.get(0)) || tr.get(1).equals(scoreNumber.get(0))) {
                    if (tr.get(0).equals("DEPOSIT")) {
                        text.add("| " + tr.get(3) + " |         DEPOSIT         " + tr.get(2));
                    } else if (tr.get(1).equals("DEBITING")) {
                        text.add("| " + tr.get(3) + " |        DEBITING        | -" + tr.get(2));
                    } else {
                        text.add("| " + tr.get(3) + " | Receipt from " + tr.get(1) + " | " + tr.get(2));
                    }
                }
            }
            text.add("---------------------------------------------------------------------");

            //Вызов метода для создания докуметов
            generatePDF(text, "statements", fileName.toString() + scoreNumber.get(0) + +localDateTime.getDayOfMonth() + "-" + localDateTime.getMonthValue() +
                    "-" + localDateTime.getYear() + "-" + localDateTime.getHour() + "-" + localDateTime.getMinute() + "-"
                    + localDateTime.getSecond());
        }
    }
}
