package PDFWorking;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PDFGenerator {
    public static void generatePDF(ArrayList<String> text) {
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
