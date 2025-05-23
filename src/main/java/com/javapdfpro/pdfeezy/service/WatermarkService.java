package com.javapdfpro.pdfeezy.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Service
public class WatermarkService {

    public File addWatermark(MultipartFile file, String watermarkText) throws IOException {
        File inputFile = File.createTempFile("input-", ".pdf");
        file.transferTo(inputFile);

        try (PDDocument document = PDDocument.load(inputFile)) {
            for (PDPage page : document.getPages()) {
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 50);
                    contentStream.setNonStrokingColor(new Color(200, 200, 200, 80)); // Light gray, semi-transparent
                    float stringWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(watermarkText) / 1000 * 50;
                    float diagonalLength = (float) Math.sqrt(page.getMediaBox().getWidth() * page.getMediaBox().getWidth()
                            + page.getMediaBox().getHeight() * page.getMediaBox().getHeight());
                    float x = (page.getMediaBox().getWidth() - stringWidth) / 2;
                    float y = page.getMediaBox().getHeight() / 2;

                    // Rotate text 45 degrees centered
                    contentStream.setTextMatrix(Matrix.getRotateInstance(Math.toRadians(45), x, y));
                    contentStream.showText(watermarkText);
                    contentStream.endText();
                }
            }

            File watermarkedFile = File.createTempFile("watermarked-", ".pdf");
            document.save(watermarkedFile);
            return watermarkedFile;
        }
    }
}
