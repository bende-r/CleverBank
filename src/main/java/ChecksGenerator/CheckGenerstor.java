package ChecksGenerator;


import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static DataOperations.CRUD.selectRow;

public class CheckGenerstor {

    public static void generateTransactionCheck(Connection connection, String inputScore, String outputScore, float deposit) {
        StringBuilder senderBank = null, recipientBank = null, depos = new StringBuilder(String.valueOf(deposit)), check = new StringBuilder();

        long seed = System.currentTimeMillis();
        LocalDateTime localDateTime = LocalDateTime.now();


        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed);

        long randomTenDigitNumber = secureRandom.nextLong() % 10000000000L;
        if (randomTenDigitNumber < 0)
            randomTenDigitNumber = -randomTenDigitNumber;

        check.append(randomTenDigitNumber);

        Statement statement = null;

        try {
            String commandRecipient = " SELECT cleverbank.banks.bank_name FROM cleverbank.banks, cleverbank.scores WHERE cleverbank.scores.bank_id = banks.id AND cleverbank.scores.score_number = '" + inputScore + "'; ";
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

        while (check.length() < 30)
            check.insert(0, ' ');

        ArrayList<String> text = new ArrayList<String>();
        text.add("--------------------------------------------------");
        text.add("|                     Bank check                 |");
        text.add("| Check:                               " + randomTenDigitNumber + " |");
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

        generateCheck(text);
    }


    private static void generateCheck(ArrayList<String> text) {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(PDType1Font.COURIER, 12);

            float marginX = 50; // Left margin
            float startY = page.getMediaBox().getHeight() - 50; // Top margin
            float lineHeight = 15; // Height of each row
            int numRows = text.size(); // Number of rows

            for (int i = 0; i < numRows; i++) {
                float y = startY - i * lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginX, y);
                contentStream.showText(text.get(i));
                contentStream.endText();
            }

            contentStream.close();

            File folder = new File("checks");
            if (!folder.exists())
                folder.mkdir();


            File file = new File("checks/" + localDateTime.getDayOfMonth() + "-" + localDateTime.getMonthValue() +
                    "-" + localDateTime.getYear() + "-" + localDateTime.getHour() + "-" + localDateTime.getMinute() + "-"
                    + localDateTime.getSecond() + ".pdf");
            document.save(file);
            document.close();

            System.out.println("PDF файл создан и сохранен в " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
