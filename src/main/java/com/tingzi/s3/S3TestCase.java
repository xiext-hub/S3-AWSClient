package com.tingzi.s3;

/**
 * @Author : Xiexiaoting
 * @Version : V 1.0
 * @Description :
 * @Modified by :
 * @Date : Created in 2022/11/11 上午11:21
 */

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.testng.annotations.AfterTest;

/**
 * junit5
 */
public class S3TestCase  extends uploadFileList {

    private Regions clientRegion = Regions.DEFAULT_REGION;

    private String accessKey = "PGPN2GQ7X5TVT4WJK14I";

    private String secretKey = "7HufQqax2yiOM+fIkt87Llw3cqYGaSsSYS1VmwI6";

    private String s3Endpoint = "https://s3gw.foreverland.xyz";

    AmazonS3 s3Client = null;



    // 初始化   在所有测试用例执行之前执行此用例  一般做数据准备
    @BeforeEach
    public void init() {
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withEndpointConfiguration(
                        new EndpointConfiguration(s3Endpoint, clientRegion.getName()))
                .build();

    }


    // 测试bucketname是否存在
    @AfterTest
    public void testBucketExist() {
        String bucketName = "xiaozhan-bucket";
        assertTrue(s3Client.doesBucketExistV2(bucketName),"bucket"+ bucketName + "doesn't exist.");
    }


    // 列出bucket中所有的bucketName
    @Test
    public void testListBucket() {
        List<Bucket> list = s3Client.listBuckets();
        list.stream().forEach(bucket -> {
            System.out.println(bucket.toString());
        });
    }


    // 测试  -创建bucket   如果bucketName已存在,则不能创建   如果未存在,则正常创建
    @Test
    public void testCreateBucket() {
        String bucketName = "xiaozhan-bucket";
        if(!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
        }else {
            System.out.println("Bucket " + bucketName + " has already exist.");
        }
    }



    @Test
    public void testCreatBucket2() {
        String bucketName = "xiaozhan-bucket";
        if(!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(new CreateBucketRequest(bucketName));
        }else {
            System.out.println("Bucket " + bucketName + " has already exist.");
        }
    }


    // 测试- 获取创建bucket时所在地区
    @Test
    public void testGetBucketLocation() {
        String bucketName = "xiaozhan-bucket";
        String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
        System.out.println("Bucket location: " + bucketLocation);
    }


    // 测试 - 删除bucket   如果bucket内文件不为空,则无法进行删除
    @Test
    public void testDeleteBucket() {
        String bucketName = "xiaozhan-bucket";
        if(s3Client.doesBucketExistV2(bucketName)) {
            //确保bucket下面没有object才能删除，否则报错409
            s3Client.deleteBucket(bucketName);

        }
    }


// test  某一个bucket的详细信息
    @Test
    public void testListObject() {
        String bucketName = "xiaozhan-bucket";
        ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
        result.getObjectSummaries().stream().forEach(object -> {
            System.out.println(object.toString());
        });
    }



    @Test
    public void testListObject2() {
        String bucketName = "xiaozhan-bucket";
        //prefix用于查询bucket下某个以某些字符串起始的key的object
        //可以用来模拟查询某个目录下所有的object的需求
        String keyProfix = "xiaozhan";
        ListObjectsV2Result result = s3Client.listObjectsV2(bucketName , keyProfix);
        result.getObjectSummaries().stream().forEach(object -> {
            System.out.println(object.toString());
        });
    }


//  测试  -上传一个普通文件
    @Test
    public void testUploadObject() {
        String bucketName = "xiaozhan-bucket";
        String key = "xiaozhan";
        String data = "my test";
        s3Client.putObject(bucketName, key , data);
    }


    // 测试  上传一张图片
    @Test
    public void testUploadObject2() {
        String bucketName = "xiaozhan-bucket";
        String key = "xiaozhan";
        File file = new File("/Users/xiexiaoting/Documents/test-photo/图片/xiaozhan/xiaozhan.jpeg");
        s3Client.putObject(bucketName, key , file);
    }


// 测试   删除bucket里的某一个文件
    @Test
    public void testDeleteObject() {
        String bucketName = "xiaozhan-bucket";
        String key = "xiaozhan";
        s3Client.deleteObject(bucketName, key);
    }


    // 测试  bucket里面的文件是否存在  返回true 和 false
    @Test
    public void testExistObject() {
        String bucketName = "xiaozhan-bucket";
        String key = "xiaozhan";
        System.out.println(s3Client.doesObjectExist(bucketName, key));
    }


    //  测试   下载文件到本地
    @Test
    public void testGetObject() {
        String bucketName = "xiaozhan-bucket";
        String key = "xiaozhan";
        //可以下载到本地
        String outputPath = "/Users/xiexiaoting/Desktop";
        try  {
            S3Object o = s3Client.getObject(bucketName, key);
            S3ObjectInputStream s3is = o.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(outputPath));
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }