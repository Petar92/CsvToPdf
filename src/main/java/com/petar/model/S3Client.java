package com.petar.model;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Component
public class S3Client {
	
	public S3Client() {}
	
	public AmazonS3 getClient() {
		return AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new PropertiesFileCredentialsProvider("/home/petar/.aws/credentials"))
				  .withRegion(Regions.EU_CENTRAL_1)
				  .build();
	}

}
