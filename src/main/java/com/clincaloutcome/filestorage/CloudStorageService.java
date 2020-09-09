package com.clincaloutcome.filestorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CloudStorageService {
  private Storage storage;
  private FileStorageProperties props;

  @Autowired
  public CloudStorageService(FileStorageProperties fileStorageProperties) {
    this.storage = StorageOptions.getDefaultInstance().getService();
    this.props = fileStorageProperties;
  }

  public void uploadObject(String objectName, String filePath) throws IOException {
    BlobId blobId = BlobId.of(this.props.getBucketName(), objectName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
    this.storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
  }

  public ArrayList<String> getBuckets() {
    Page<Bucket> buckets = this.storage.list();

    ArrayList<String> names = new ArrayList<String>();
    for (Bucket bucket : buckets.iterateAll()) {
      names.add(bucket.getName());
    }

    return names;
  }

  public ArrayList<String> getObjectsFromBucket() {
    Bucket bucket = this.storage.get(this.props.getBucketName());
    Page<Blob> blobs = bucket.list();

    ArrayList<String> names = new ArrayList<String>();
    for (Blob blob : blobs.iterateAll()) {
      names.add(blob.getName());
    }

    return names;
  }
}
