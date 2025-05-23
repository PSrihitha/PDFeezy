package com.javapdfpro.pdfeezy.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfToWordService {

    public File convertPdfToWord(MultipartFile file) throws IOException {
        File output = File.createTempFile("converted_", ".docx");

        try (PDDocument pdfDoc = PDDocument.load(file.getInputStream());
             XWPFDocument docx = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(output)) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(pdfDoc);

            XWPFParagraph paragraph = docx.createParagraph();
            paragraph.createRun().setText(text);

            docx.write(fos);
        }

        return output;
    }
}
