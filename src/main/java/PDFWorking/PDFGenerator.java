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

/**
 * Класс для создания PDF документов
 *
 * @author Богдан Рыбаков
 * @version 1.0
 */

public class PDFGenerator {

    /**
     * Метод для создания PDF-документа
     *
     * @param text       - текст документа
     * @param folderName - папка, в которой будет размещаться файл
     * @param fileName   - имя создаваемого файла
     */
    public static void generatePDF(ArrayList<String> text, String folderName, String fileName) {
        try {
            //Создание документа
            PDDocument document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(PDType1Font.COURIER, 12);

            float marginX = 50;
            float startY = page.getMediaBox().getHeight() - 50;
            float lineHeight = 15;
            int numRows = text.size();

            //Заполнение документа текстом
            for (int i = 0; i < numRows; i++) {
                float y = startY - i * lineHeight;
                contentStream.beginText();
                contentStream.newLineAtOffset(marginX, y);
                contentStream.showText(text.get(i));
                contentStream.endText();
            }

            contentStream.close();

            //Создание папки, если она не существует
            File folder = new File(folderName);
            if (!folder.exists())
                folder.mkdir();

            //Создание и сохранение документа
            File file = new File(folderName + "/" + fileName + ".pdf");
            document.save(file);
            document.close();

            System.out.println("PDF-file is created and saved in " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
