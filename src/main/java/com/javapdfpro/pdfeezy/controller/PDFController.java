package com.javapdfpro.pdfeezy.controller;

import com.javapdfpro.pdfeezy.service.*;
import com.javapdfpro.pdfeezy.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class PDFController {

    @Autowired
    private SplitService splitService;

    @Autowired
    private WatermarkService watermarkService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private PdfToWordService wordService;

    @Autowired
    private PdfToImageService imageService;

    @Autowired
    private PDFEditService pdfEditService;

    @Autowired
    private PdfUnlockService pdfUnlockService;

    @Autowired
    private PDFPageNumberService pdfPageNumberService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/split")
    public ResponseEntity<Resource> splitPdf(@RequestParam("file") MultipartFile file,
                                             @RequestParam("page") int pageNumber) throws IOException {
        File splitFile = splitService.splitPdf(file, pageNumber);
        return FileUtil.prepareFileResponse(splitFile, "split_page_" + pageNumber + ".pdf");
    }

    @PostMapping("/watermark")
    public ResponseEntity<Resource> addWatermark(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("text") String text) throws IOException {
        File watermarkedFile = watermarkService.addWatermark(file, text);
        return FileUtil.prepareFileResponse(watermarkedFile, "watermarked.pdf");
    }

    @PostMapping("/protect")
    public ResponseEntity<Resource> protectPdf(@RequestParam("file") MultipartFile file,
                                               @RequestParam("password") String password) throws IOException {
        File protectedFile = passwordService.protectPdf(file, password);
        return FileUtil.prepareFileResponse(protectedFile, "protected.pdf");
    }
    @PostMapping("/convert-to-word")
    public ResponseEntity<Resource> convertToWord(@RequestParam("file") MultipartFile file) throws IOException {
        File docxFile = wordService.convertPdfToWord(file);
        return FileUtil.prepareFileResponse(docxFile, "converted.docx");
    }

    @PostMapping("/convert-to-images")
    public ResponseEntity<Resource> convertToImages(@RequestParam("file") MultipartFile file) throws IOException {
        List<File> images = imageService.convertPdfToImages(file);
        return FileUtil.prepareZipResponse(images, "images.zip");
    }

    @PostMapping("/edit")
    public ResponseEntity<Resource> editPdf(@RequestParam("file") MultipartFile file,
                                            @RequestParam("text") String text) throws IOException {
        File editedFile = pdfEditService.addTextToPdf(file, text);
        return FileUtil.prepareFileResponse(editedFile, "edited.pdf");
    }

    @PostMapping("/unlock")
    public ResponseEntity<Resource> unlockPdf(@RequestParam("file") MultipartFile file,
                                              @RequestParam("password") String password) throws IOException {
        File unlockedFile = pdfUnlockService.unlockPdf(file, password);
        return FileUtil.prepareFileResponse(unlockedFile, "unlocked.pdf");
    }

    @PostMapping("/add-page-numbers")
    public ResponseEntity<Resource> addPageNumbers(@RequestParam("file") MultipartFile file) throws IOException {
        File result = pdfPageNumberService.addPageNumbers(file);
        return FileUtil.prepareFileResponse(result, "numbered.pdf");
    }


}
