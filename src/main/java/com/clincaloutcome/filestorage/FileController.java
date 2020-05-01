package com.clincaloutcome.filestorage;

import com.clincaloutcome.builder.WebBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private WebBuilder webBuilder;

    @PostMapping("/uploadSearchQuery")
    public UploadFileResponse submitSearchQuery(@RequestParam("searchQuery") String searchQuery, @RequestParam("pageNumber") String pageNumber) throws IOException {
        webBuilder.singleBuilder(searchQuery, pageNumber);
        MultipartFile multipartFileToSend = getMultipartFile();
        String fileName = fileStorageService.storeFile(multipartFileToSend);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, multipartFileToSend.getContentType(), multipartFileToSend.getSize());
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile bulk) throws IOException {
        webBuilder.bulkBuilder(multipartFileToFile(bulk));
        MultipartFile multipartFileToSend = getMultipartFile();
        String fileName = fileStorageService.storeFile(multipartFileToSend);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, bulk.getContentType(), bulk.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<String> listOfFilesNames = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            File file = new File(multipartFile.getName());
            InputStream stream = new FileInputStream(file);
            MockMultipartFile mockMultipartFile = new MockMultipartFile(file.getName(), file.getName(), MediaType.ALL_VALUE, stream);
            String fileName = fileStorageService.storeFile(mockMultipartFile);
            listOfFilesNames.add(fileName);
        }
        int count = 0;
        String file1 = listOfFilesNames.get(count);
        String file2 = listOfFilesNames.get(count + 1);

        // Run Excel Builder
        webBuilder.crossBuilder(file1, file2);

        // Load from file name.
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path("MatchedEUClinicalTrails.xlsx")
                .toUriString();

        return Collections.singletonList(new UploadFileResponse("MatchedEUClinicalTrails", fileDownloadUri));
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


    private MultipartFile getMultipartFile() throws IOException {
        File file = new File("./uploads/EUClinicalTrails.xlsx");
        InputStream stream = new FileInputStream(file);
        return new MockMultipartFile("EUClinicalTrails", file.getName(), MediaType.ALL_VALUE, stream);
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
