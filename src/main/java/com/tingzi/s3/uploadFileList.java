package com.tingzi.s3;

/**
 * @Author : Xiexiaoting
 * @Version : V 1.0
 * @Description :
 * @Modified by :
 * @Date : Created in 2023/7/13 上午11:25
 */


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;


public class uploadFileList {

    /**
     * access_key_id 访问密钥ID
     */
    public static final String ACCESS_KEY = "PGPN2GQ7X5TVT4WJK14I";
    /**
     * secret_key 访问密钥
     */
    public static final String SECRET_KEY = "7HufQqax2yiOM+fIkt87Llw3cqYGaSsSYS1VmwI6";
    /**
     * bucket_name 桶名
     */
    public static final String BUCKET_NAME = "xiaozhan-bucket";
    /**
     * 文件夹
     */
    static final String PATH="/Users/xiexiaoting/Documents/test-photo/图片/xiaozhan";


    private String s3Endpoint = "https://s3gw.foreverland.xyz";

@Test
    public void uploadDocuments(List<File> filesToUpload) throws
            AmazonServiceException, AmazonClientException,
            InterruptedException {
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(getCredentials()).withRegion(Regions.AP_SOUTH_1)
                .build();

        TransferManager transfer = TransferManagerBuilder.standard().withS3Client(s3).build();
        String bucket = BUCKET_NAME;

        MultipleFileUpload upload = transfer.uploadFileList(bucket, "xiaozhan", new File(PATH), filesToUpload);
        upload.waitForCompletion();
    }

    private AWSCredentialsProvider getCredentials() {
        String accessKey = ACCESS_KEY;
        String secretKey = SECRET_KEY;
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return new AWSStaticCredentialsProvider(awsCredentials);

    }


    public static void main(String[] args) {

    }


}

