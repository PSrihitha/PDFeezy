package com.javapdfpro.pdfeezy.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PDFEditService {

    public File addTextToPdf(MultipartFile file, String text) throws IOException {
        File output = File.createTempFile("edited_", ".pdf");

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            int lastPageIndex = document.getNumberOfPages() - 1;
            PDPage lastPage = document.getPage(lastPageIndex);
            PDRectangle mediaBox = lastPage.getMediaBox();

            // Extract existing text from last page
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(lastPageIndex + 1);
            stripper.setEndPage(lastPageIndex + 1);
            String existingText = stripper.getText(document);

            int lineCount = existingText.split("\r\n|\r|\n").length;
            float lineHeight = 16f; // Estimated line height for font size 14
            float fontSize = 14f;

            float margin = 50f;
            float x = margin;
            float y = mediaBox.getHeight() - (lineCount * lineHeight) - margin;

            // Don't write below page bottom
            if (y < margin) y = margin;

            try (PDPageContentStream contentStream = new PDPageContentStream(
                    document, lastPage, PDPageContentStream.AppendMode.APPEND, true, true)) {

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
                contentStream.newLineAtOffset(x, y);
                contentStream.showText(text);
                contentStream.endText();
            }

            document.save(output);
        }

        return output;
    }
}
