package com.clincaloutcome.filestorage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileDownloadUri) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
    }
}
