package com.clincaloutcome.fileStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

//    @Autowired
//    private ScorecardService scorecardService;


    @PostMapping("/uploadSearchQuery")
    public UploadFileResponse submitSearchQuery(@RequestParam("searchQuery") String searchQuery, @RequestParam("pageNumber") String pageNumber) {
        System.out.println("Url: " + searchQuery);
        System.out.println("Page Number: " + pageNumber);
        //   String fileName = fileStorageService.storeFile(file);

//        Scorecard scorecard = convertExcelToScorecard(fileName);
//
//        scorecard = scorecardService.balanceScoreCard(scorecard);
//        String newFile = excelWriter.createNewExcelFile(scorecard);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path("ClinicalTrial")
                .toUriString();

        return new UploadFileResponse("Clinical", fileDownloadUri);
    }

//    protected Scorecard convertExcelToScorecard(String excel) {
//        return excelReader.readFile(excel);
//    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

//        Scorecard scorecard = convertExcelToScorecard(fileName);
//
//        scorecard = scorecardService.balanceScoreCard(scorecard);
//        String newFile = excelWriter.createNewExcelFile(scorecard);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(file))
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

//    protected Scorecard convertExcelToScorecard(String excel) {
//        return excelReader.readFile(excel);
//    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
