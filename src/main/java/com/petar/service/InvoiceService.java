package com.petar.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.petar.model.Invoice;

@Component
public class InvoiceService {
	
	@Value("${aws_access_key}")
	private String accessKey;
	
	@Value("${aws_secret_keyl}")
	private String securityKey;
	
	AWSCredentials credentials = new BasicAWSCredentials(
			accessKey, 
			securityKey
			);
	
	AmazonS3 s3client = AmazonS3ClientBuilder
			  .standard()
			  .withCredentials(new AWSStaticCredentialsProvider(credentials))
			  .withRegion(Regions.EU_CENTRAL_1)
			  .build();
	
	
	
	List<Invoice> invoices = new CopyOnWriteArrayList<>();
	
	public Invoice create(URL url) {
		Invoice invoice = new Invoice(url);
		invoices.add(invoice);
		return invoice;
	}
	
	public List<Invoice> getAllInvoices() {
		return invoices;
	}
	
	public Invoice getInvoice(String id) {
		return null;
	}
	
	public Invoice store(MultipartFile file) throws IOException {
		File s3File = convert(file).toFile();
		s3client.putObject(
				  "csv-to-pdf-invoices", 
				  "Document/test.csv",
				  s3File
				);
		URL url = s3client.getUrl("csv-to-pdf-invoices", "Document/test.csv");
		return new Invoice(url);
	}
	
	private static Path convert(MultipartFile file) throws IOException {
		  Path newFile = Paths.get(file.getOriginalFilename());
		  try(InputStream is = file.getInputStream();
		     OutputStream os = Files.newOutputStream(newFile)) {
		     byte[] buffer = new byte[4096];
		     int read = 0;
		     while((read = is.read(buffer)) > 0) {
		       os.write(buffer,0,read);
		     }
		  }
		  return newFile;  
		}

}
