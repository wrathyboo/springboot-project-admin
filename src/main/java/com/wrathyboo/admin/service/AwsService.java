package com.wrathyboo.admin.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.BasicAWSCredentials;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwsService {
	

	
//	AmazonS3 s3client = AmazonS3ClientBuilder
//			  .standard()
//			  .withCredentials(new AWSStaticCredentialsProvider(credentials))
//			  .withRegion(Regions.AP_SOUTHEAST_1)
//			  .build();
//    
	
//	public void bucketList() {
//		List<Bucket> buckets = s3client.listBuckets();
//		for(Bucket bucket : buckets) {
//		    System.out.println(bucket.getName());
//		}
//	}
//	
//	public void uploadFile(String fileName, InputStream inputStream) {
//		s3client.putObject(
//				  "wrathyboo-bucket", 
//				  fileName, 
//				  new File(Path)
//				);
//	}
	 public static void uploadFile(String fileName, InputStream inputStream)
	            throws S3Exception, AwsServiceException, SdkClientException, IOException {
	        S3Client client = S3Client.builder()
	        		.region(Region.AP_NORTHEAST_1)
	        		.build();
	         
	        PutObjectRequest request = PutObjectRequest.builder()
	                            .bucket("wrathyboo-bucket")
	                            .key(fileName)
	                            .acl("public-read")
	                            .build();
	         
	        client.putObject(request,
	                RequestBody.fromInputStream(inputStream, inputStream.available()));
	     
	    }
	 
}
