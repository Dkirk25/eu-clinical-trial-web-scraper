package com.clincaloutcome.filestorage;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CloudStorageService {
    private Storage storage;
    private FileStorageProperties props;

    @Autowired
    public CloudStorageService(FileStorageProperties fileStorageProperties) {
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.props = fileStorageProperties;
    }

    public FileStorageProperties getProps() {
        return this.props;
    }

    public String uploadObject(String fileName, byte[] bytes, String contentType) {
        String objectName = StringUtils.cleanPath(Objects.requireNonNull(fileName));
        BlobId blobId = BlobId.of(this.props.getBucketName(), objectName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

        this.storage.create(blobInfo, bytes);
        return objectName;
    }

    public String getObjectDetails(String objectName) {
        return this.storage.get(BlobId.of(this.props.getBucketName(), objectName)).getSelfLink();
    }

    public List<String> getBuckets() {
        Page<Bucket> buckets = this.storage.list();

        ArrayList<String> names = new ArrayList<>();
        for (Bucket bucket : buckets.iterateAll()) {
            names.add(bucket.getName());
        }
        return names;
    }

    public List<String> getObjectsFromBucket() {
        Bucket bucket = this.storage.get(this.props.getBucketName());
        Page<Blob> blobs = bucket.list();

        ArrayList<String> names = new ArrayList<>();
        for (Blob blob : blobs.iterateAll()) {
            names.add(blob.getName());
        }
        return names;
    }
}
