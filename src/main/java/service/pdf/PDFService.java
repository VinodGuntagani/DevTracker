package service.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.servlet.ServletContext;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class PDFService {

    public byte[] generateLessonPDF(String html, String css, ServletContext context) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();

        // Load Japanese font
        String fontPath = context.getRealPath("/fonts/NotoSansJP-Regular.ttf");

        if (fontPath != null) {

            File fontFile = new File(fontPath);

            if (fontFile.exists()) {
                builder.useFont(fontFile, "Noto Sans JP");
                System.out.println("✓ Japanese font loaded");
            } else {
                System.out.println("Japanese font file not found: " + fontPath);
            }

        } else {
            System.out.println("Unable to resolve font path.");
        }

        // Clean HTML
        String cleanedHtml = PDFHtmlCleaner.clean(html);

        // Remove inline font-family declarations
        cleanedHtml = cleanedHtml.replaceAll(
                "(?i)font-family\\s*:[^;\"']*;?",
                ""
        );

        // Force Japanese font for every Japanese character sequence
        cleanedHtml = cleanedHtml.replaceAll(
                "([\\p{IsHiragana}\\p{IsKatakana}\\p{IsHan}]+)",
                "<span style=\"font-family:'Noto Sans JP'\">$1</span>"
        );

        String finalHtml = """
                <!DOCTYPE html>
                <html xmlns="http://www.w3.org/1999/xhtml">
                <head>
                <meta charset="UTF-8"/>

                <style>

                *{
                    font-family:"Noto Sans JP", Arial, sans-serif !important;
                }

                %s

                </style>

                </head>

                <body>

                %s

                </body>
                </html>
                """.formatted(css, cleanedHtml);

        builder.withHtmlContent(finalHtml, null);
        builder.toStream(outputStream);

        builder.run();

        return outputStream.toByteArray();
    }
}