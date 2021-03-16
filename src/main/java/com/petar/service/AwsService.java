package com.petar.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.petar.model.Csv;

@Component
public class AwsService {
	
	AmazonS3 s3client = AmazonS3ClientBuilder
			  .standard()
			  .withCredentials(new PropertiesFileCredentialsProvider("/home/petar/.aws/credentials"))
			  .withRegion(Regions.EU_CENTRAL_1)
			  .build();
	
	public Csv storeCsv(MultipartFile file) throws IOException {
		File s3File = convert(file);
		s3client.putObject(
				  "csv-to-pdf-invoices", 
				  "Document/test.csv",
				  s3File
				);
		URL url = s3client.getUrl("csv-to-pdf-invoices", "Document/test.csv");
		return new Csv(url);
	}
	
	public S3Object getCsv(String id) {
		return s3client.getObject("csv-to-pdf-invoices", "Document/test.csv");
	}
	
	
	//helper method to convert MultipartFile to File 
	private static File convert(MultipartFile file) throws IOException {
		  Path newFile = Paths.get(file.getOriginalFilename());
		  try(InputStream is = file.getInputStream();
		     OutputStream os = Files.newOutputStream(newFile)) {
		     byte[] buffer = new byte[4096];
		     int read = 0;
		     while((read = is.read(buffer)) > 0) {
		       os.write(buffer,0,read);
		     }
		  }
		  return newFile.toFile();  
	}

}
