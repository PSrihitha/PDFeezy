package com.javapdfpro.pdfeezy.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PdfUnlockService {

    public File unlockPdf(MultipartFile file, String password) throws IOException {
        File output = File.createTempFile("unlocked_", ".pdf");

        try (PDDocument document = PDDocument.load(file.getInputStream(), password)) {
            document.setAllSecurityToBeRemoved(true);
            document.save(output);
        }

        return output;
    }
}
