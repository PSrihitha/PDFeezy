package com.javapdfpro.pdfeezy.service;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class SplitService {

    public File splitPdf(MultipartFile file, int pageNumber) throws IOException {
        File inputFile = File.createTempFile("input-", ".pdf");
        file.transferTo(inputFile);

        try (PDDocument document = PDDocument.load(inputFile)) {
            if (pageNumber < 1 || pageNumber > document.getNumberOfPages()) {
                throw new IllegalArgumentException("Page number out of range");
            }

            // Extract requested page into a new PDF
            PDDocument singlePageDoc = new PDDocument();
            singlePageDoc.addPage(document.getPage(pageNumber - 1));

            File splitFile = File.createTempFile("split-page-" + pageNumber + "-", ".pdf");
            singlePageDoc.save(splitFile);
            singlePageDoc.close();

            return splitFile;
        }
    }
}
