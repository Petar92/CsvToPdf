package com.petar.model;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Component
public class S3Client {
	
	private AmazonS3 s3client;
	
	public S3Client() {}
	
	public void initS3Client(String pathToCredentials) {
		System.out.println("PATH " + pathToCredentials);
		this.s3client= AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new PropertiesFileCredentialsProvider(pathToCredentials))
				  .withRegion(Regions.EU_CENTRAL_1)
				  .build();
	}
	
	public AmazonS3 getS3client() {
		System.out.println("getting client");
		return s3client;
	}

}
