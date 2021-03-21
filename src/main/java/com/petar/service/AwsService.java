package com.petar.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.PropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.petar.model.Csv;

@Component
public class AwsService {
	
	AmazonS3 s3client = AmazonS3ClientBuilder
			  .standard()
			  .withCredentials(new PropertiesFileCredentialsProvider("/home/petar/.aws/credentials"))
			  .withRegion(Regions.EU_CENTRAL_1)
			  .build();
	
	public Csv storeCsv(MultipartFile multipartFile) throws IOException {
		String fileName = multipartFile.getOriginalFilename();
		File s3File = convertToFile(multipartFile);
		s3client.putObject(
				  "csv-to-pdf-invoices", 
				  "Document/csvs/" + fileName,
				  s3File
				);
		URL url = s3client.getUrl("csv-to-pdf-invoices", "Document/csvs/" + fileName);
		return new Csv(url);
	}
	
	public S3Object getCsv(String fileName) throws IOException {
		S3Object s3object = s3client.getObject("csv-to-pdf-invoices", "Document/csvs/" + fileName);
		return s3object; 
	}
	
	public S3Object getPdf(String fileName) throws IOException {
		S3Object s3object = s3client.getObject("csv-to-pdf-invoices", "Document/pdfs/" + fileName);
		return s3object; 
	}
	
	public Map<String, URL> getAllPdfs() throws IOException {
		Map<String, URL> map = new HashMap<String, URL>();
		for ( S3ObjectSummary summary : S3Objects.withPrefix(s3client, "csv-to-pdf-invoices", "Document/pdfs/") ) {
			
	        try {

	            // Set the presigned URL to expire after one hour.
	            java.util.Date expiration = new java.util.Date();
	            long expTimeMillis = expiration.getTime();
	            expTimeMillis += 1000 * 60 * 60;
	            expiration.setTime(expTimeMillis);

	            // Generate the presigned URL.
	            System.out.println("Generating pre-signed URL.");
	            GeneratePresignedUrlRequest generatePresignedUrlRequest =
	                    new GeneratePresignedUrlRequest("csv-to-pdf-invoices", summary.getKey())
	                            .withMethod(HttpMethod.GET)
	                            .withExpiration(expiration);
	            URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

	            System.out.println("Pre-Signed URL: " + url.toString());
	            
	            map.put(summary.getKey().replace("Document/pdfs/", ""), url);
	        } catch (AmazonServiceException e) {
	            // The call was transmitted successfully, but Amazon S3 couldn't process 
	            // it, so it returned an error response.
	            e.printStackTrace();
	        } catch (SdkClientException e) {
	            // Amazon S3 couldn't be contacted for a response, or the client
	            // couldn't parse the response from Amazon S3.
	            e.printStackTrace();
	        }
		}
		return map;
	}
	
	
	//helper method to convert MultipartFile to File 
	private static File convertToFile(MultipartFile file) throws IOException {
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
