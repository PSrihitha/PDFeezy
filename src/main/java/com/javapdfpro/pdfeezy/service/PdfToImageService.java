package com.javapdfpro.pdfeezy.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfToImageService {

    public List<File> convertPdfToImages(MultipartFile file) throws IOException {
        List<File> imageFiles = new ArrayList<>();

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage image = renderer.renderImageWithDPI(page, 300);
                File imageFile = File.createTempFile("page_" + (page + 1) + "_", ".jpg");
                ImageIO.write(image, "JPEG", imageFile);
                imageFiles.add(imageFile);
            }
        }

        return imageFiles;
    }
}
