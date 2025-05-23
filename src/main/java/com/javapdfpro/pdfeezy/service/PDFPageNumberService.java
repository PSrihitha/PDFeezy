package com.javapdfpro.pdfeezy.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PDFPageNumberService {

    public File addPageNumbers(MultipartFile file) throws IOException {
        File outputFile = File.createTempFile("numbered_", ".pdf");

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            int totalPages = document.getNumberOfPages();

            for (int i = 0; i < totalPages; i++) {
                PDPage page = document.getPage(i);
                PDRectangle mediaBox = page.getMediaBox();

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
                        PDPageContentStream.AppendMode.APPEND, true)) {

                    String text = "Page " + (i + 1) + " of " + totalPages;

                    float fontSize = 10;
                    float stringWidth = PDType1Font.HELVETICA.getStringWidth(text) / 1000 * fontSize;
                    float centerX = (mediaBox.getWidth() - stringWidth) / 2;
                    float marginY = 20; // distance from bottom

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, fontSize);
                    contentStream.newLineAtOffset(centerX, marginY);
                    contentStream.showText(text);
                    contentStream.endText();
                }
            }

            document.save(outputFile);
        }

        return outputFile;
    }
}
