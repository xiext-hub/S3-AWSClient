package com.tingzi.test;

import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.nio.file.Paths;

/**
 *
 */

public class BucketOperation {

    private final S3Client s3Client;

    public BucketOperation(URI endpoint) {
        System.setProperty("aws.accessKeyId", "cf1178bb394746f1b4f4ea2f3e7d58bd");
        System.setProperty("aws.secretAccessKey", "aaIrcpUOryreVFL1yGjOk4JSCLoIH9uL2EHVBbYU");
        s3Client = S3Client.builder()
                .endpointOverride(URI.create("https://s3gw.foreverland.xyz"))
                .region(Region.US_EAST_1)
                .credentialsProvider(SystemPropertyCredentialsProvider.create())
                .build();
    }

    public CopyObjectResponse copyObject(CopyObjectRequest copyObjectRequest) {
        return s3Client.copyObject(copyObjectRequest);
    }

    public ListBucketsResponse listBuckets() {
        return s3Client.listBuckets();
    }

    public DeleteObjectResponse deleteObject(DeleteObjectRequest deleteObjectRequest) {
        return s3Client.deleteObject(deleteObjectRequest);
    }

    public PutObjectResponse putObject(PutObjectRequest putObjectRequest, String path) {
        try {
            return s3Client.putObject(putObjectRequest, Paths.get(path));
        } catch (Exception e) {
          //  log.error("upload failed:{}", e.getMessage(), e);
            System.out.println("upload failed:{}" + e.getMessage());
            return null;
        }
    }



}