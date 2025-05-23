package com.javapdfpro.pdfeezy.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PasswordService {

    public File protectPdf(MultipartFile file, String password) throws IOException {
        File inputFile = File.createTempFile("input-", ".pdf");
        file.transferTo(inputFile);

        try (PDDocument document = PDDocument.load(inputFile)) {
            AccessPermission ap = new AccessPermission();
            StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
            spp.setEncryptionKeyLength(128);
            spp.setPermissions(ap);

            document.protect(spp);

            File protectedFile = File.createTempFile("protected-", ".pdf");
            document.save(protectedFile);
            return protectedFile;
        }
    }
}
