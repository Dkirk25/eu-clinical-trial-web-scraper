package com.clincaloutcome.filestorage;

import com.google.api.gax.paging.Page;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
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

    public String uploadObject(String fileName, PipedOutputStream stream, String contentType) {
        String objectName = StringUtils.cleanPath(Objects.requireNonNull(fileName));
        BlobId blobId = BlobId.of(this.props.getBucketName(), objectName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        try (WriteChannel writer = storage.writer(blobInfo)) {
            PipedInputStream input = new PipedInputStream(stream);

            int data = input.read();
            while(data != -1) {
                byte[] bytes = new byte[]{(byte) data};
                writer.write(ByteBuffer.wrap(bytes, 0, 1));
                data = input.read();
            }

            input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
        // this.storage.create(blobInfo, stream.toByteArray());
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
