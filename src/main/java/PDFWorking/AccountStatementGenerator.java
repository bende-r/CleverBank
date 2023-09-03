package PDFWorking;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static PDFWorking.PDFGenerator.generatePDF;

public class AccountStatementGenerator {
    public static void generateAccountStatement(Connection connection, int user_id, LocalDateTime startOfPeriod, LocalDateTime endOfPeriod) {

        StringBuilder checkNumber = new StringBuilder(), FIO = new StringBuilder();
        ArrayList<String> scoreNumbers = new ArrayList<>();
        ArrayList<ArrayList<String>> transactions = new ArrayList<>();

        LocalDateTime localDateTime = LocalDateTime.now();

        Statement statement = null;

        try {
            String commandUserFIO = " SELECT * FROM cleverbank.users WHERE cleverbank.users.id = " + user_id + "; ";
            String commandAllScoreNumbers = "SELECT cleverbank.scores.score_number FROM cleverbank.users, cleverbank.scores WHERE cleverbank.scores.user_id = " + user_id + " AND users.id = " + user_id + ";";


            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(commandUserFIO);
            resultSet.next();

            FIO.append(resultSet.getString(2) + " ");
            FIO.append(resultSet.getString(3) + " ");
            FIO.append(resultSet.getString(4));

            resultSet = statement.executeQuery(commandAllScoreNumbers);
            while (resultSet.next())
                scoreNumbers.add(resultSet.getString(1));
            resultSet.close();

            for (String number : scoreNumbers) {
                ArrayList<String> arr = new ArrayList<>();
                String command = "SELECT sender_score_number,recipient_score_number, transfer_amount, date FROM cleverbank.transactions WHERE transactions.sender_score_number = '" + number + "' OR transactions.recipient_score_number ='" + number + "';";
                resultSet = statement.executeQuery(command);

                while (resultSet.next()) {
                    for (int i = 1; i <= 3; i++)
                        System.out.print(resultSet.getString(i) + "\t");
                    System.out.println();
                }
               /* while (resultSet.next()) {
                    for (int i = 0; i <= 3; i++) {
                        arr.add(resultSet.getString(i));
                    }
                }*/
                // transactions.add(arr);
            }

            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while (FIO.length() < 40)
            FIO.insert(0, ' ');

     /*   while (startOfPeriodSB.length() < 32)
            startOfPeriodSB.insert(0, ' ');

        while (endOfPeriodSB.length() < 29)
            endOfPeriodSB.insert(0, ' ');*/

        while (checkNumber.length() < 40)
            checkNumber.insert(0, ' ');

/*
        ArrayList<String> text = new ArrayList<String>();
        text.add("--------------------------------------------------");
        text.add("|                     Account Statement                 |");
        text.add("| Сlient:" + FIO + " |");
        text.add("| Score number:" +scoreNumbers[]);
        text.add("| Period: "+startOfPeriod.getDayOfMonth()+"."+startOfPeriod.getMonthValue()+"."+startOfPeriod.getYear()
                +" - "+endOfPeriod.getDayOfMonth()+"."+endOfPeriod.getMonthValue()+"."+endOfPeriod.getYear());
        text.add("| Balance: "+scoreNumbers);
text.add("--------------------------------------------------------------");
text.add("       Date        |         Note         |       Amount      ");
text.add("--------------------------------------------------------------");
for(){
    text.add();
}*/


        // generatePDF(text);
    }
}
