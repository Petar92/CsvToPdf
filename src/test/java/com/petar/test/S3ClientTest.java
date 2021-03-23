package com.petar.test;

import org.junit.BeforeClass;
import org.mockito.Mockito;

import com.amazonaws.services.s3.AmazonS3;

public class S3ClientTest {
	
    @BeforeClass
    public static void initializeAmazonS3ClientBuilder() {
    	AmazonS3 s3client = Mockito.mock(AmazonS3.class);
    }

}
