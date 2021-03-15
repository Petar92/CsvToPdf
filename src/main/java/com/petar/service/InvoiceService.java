package com.petar.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.petar.model.Invoice;

@Component
public class InvoiceService {
	
	
	AWSCredentials credentials = new BasicAWSCredentials(
			  "AKIA6GRIQI6QCMMNVT7J", 
			  "mOyPliqQQUwJ/4KTuBUWYbccSuROdf/Lz/+5qjGB"
			);
	
	AmazonS3 s3client = AmazonS3ClientBuilder
			  .standard()
			  .withCredentials(new AWSStaticCredentialsProvider(credentials))
			  .withRegion(Regions.EU_CENTRAL_1)
			  .build();
	
	List<Invoice> invoices = new CopyOnWriteArrayList<>();
	
	public Invoice create(String url) {
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
	
	public Invoice store(MultipartFile file) {
		return null;
	}

}
