package com.petar.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.s3.model.S3Object;
import com.itextpdf.text.DocumentException;
import com.petar.model.S3Client;
import com.petar.service.AwsService;
import com.petar.service.ConverterService;

@Controller
public class InvoiceController {
	
	private final AwsService awsService;
	private final ConverterService converterService;
	private final S3Client s3client;
	
	public InvoiceController(AwsService awsService, ConverterService converterService, S3Client s3client) {
		this.awsService = awsService;
		this.converterService = converterService;
		this.s3client = s3client;
	}
	
	@GetMapping("/")
	public String home() {
		return "index.html";
	}
	
	@GetMapping("/invoice/{name}")
	public String getInvoice(@PathVariable String name, RedirectAttributes attributes) throws IOException {
		System.out.println("NAME " + name);
		S3Object s3object = awsService.getPdf(name+".pdf");
		InputStream fileInputStream = s3object.getObjectContent();
		String home = System.getProperty("user.home");
		File targetFile = new File(home+"/Downloads/" + name);
	    FileUtils.copyInputStreamToFile(fileInputStream, targetFile);
	    attributes.addFlashAttribute("message", "File " + name + " downloaded!");
	    return "redirect:/";
	}
	
	@GetMapping("/invoices")
	public String getInvoices(Model model) throws IOException {
		//Map<String, URL> invoices = awsService.getAllPdfs();
		//****************************************************
		Map<String, URL> invoices = new HashMap<String, URL>();
		URL url = new URL("https://stackoverflow.com/questions/23144358/how-to-loop-through-map-in-thymeleaf");
		invoices.put("test", url);
		//****************************************************
		model.addAttribute("invoices", invoices);
	    return "index.html";
	}
	
	@PostMapping("/keys")
	public String uploadKeys(RedirectAttributes attributes) throws IOException, DocumentException {
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    s3client.initS3Client(selectedFile.getAbsolutePath());
		 // return success response
	        attributes.addFlashAttribute("message", "Keys successfully uploaded");
		}
        
        return "redirect:/";
	}
	
	@PostMapping("/uploads")
	public String uploadCsv(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) throws IOException, DocumentException {
        
        awsService.storeCsv(file);
		converterService.convert(file);
		
        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded the file!");

        return "redirect:/";
	}

}
