package com.clincaloutcome.filestorage;

import com.clincaloutcome.builder.WebBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private CloudStorageService cloudStorage;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private WebBuilder webBuilder;

    @GetMapping("/buckets")
    public List<String> getBuckets() {
        return cloudStorage.getBuckets();
    }

    @GetMapping("/files")
    public List<String> getFiles() {
        return cloudStorage.getObjectsFromBucket();
    }

    @PostMapping("/uploadSearchQuery")
    public UploadFileResponse submitSearchQuery(@RequestParam("searchQuery") String searchQuery, @RequestParam("pageNumber") String pageNumber) {
        ByteArrayOutputStream stream = webBuilder.singleBuilder(searchQuery, pageNumber);
        String fileName = cloudStorage.uploadObject("eu-clinical-report_" + System.currentTimeMillis() + ".xlsx", stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String uri = "https://storage.googleapis.com/" + this.cloudStorage.getProps().getBucketName() + "/" + fileName;
        return new UploadFileResponse(fileName, uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 0);
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile bulk) throws IOException {
        ByteArrayOutputStream stream = webBuilder.bulkBuilder(multipartFileToFile(bulk));
        String fileName = cloudStorage.uploadObject("eu-clinical-bulk-report_" + System.currentTimeMillis() + ".xlsx", stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String uri = "https://storage.googleapis.com/" + this.cloudStorage.getProps().getBucketName() + "/" + fileName;
        return new UploadFileResponse(fileName, uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 0);
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<File> listOfFilesNames = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            File file = new File(multipartFile.getName());
            listOfFilesNames.add(file);
        }
        int count = 0;
        File file1 = listOfFilesNames.get(count);
        File file2 = listOfFilesNames.get(count + 1);

        ByteArrayOutputStream streamFile = webBuilder.crossBuilder(file1, file2);
        String fileName = cloudStorage.uploadObject("matched-clinical-report_" + System.currentTimeMillis() + ".xlsx", streamFile, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String uri = "https://storage.googleapis.com/" + this.cloudStorage.getProps().getBucketName() + "/" + fileName;

        return Collections.singletonList(new UploadFileResponse("MatchedEUClinicalTrails", uri));
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
            LOGGER.info("Could not determine file type.");
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

    public static File multipartFileToFile(MultipartFile file) throws IOException {
        if (file != null && file.getOriginalFilename() != null) {
            File convFile = new File(file.getOriginalFilename());
            if (convFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(file.getBytes());
                } catch (Exception e) {
                    LOGGER.error("Cannot write file.", e);
                }
            }
            return convFile;
        }
        return null;
    }
}
