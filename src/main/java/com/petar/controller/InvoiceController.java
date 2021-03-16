package com.petar.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.s3.model.S3Object;
import com.petar.service.AwsService;

@Controller
public class InvoiceController {
	
	private final AwsService awsService;
	
	public InvoiceController(AwsService awsService) {
		this.awsService = awsService;
	}
	
	@GetMapping("/")
	public String home() {
		return "index.html";
	}
	
	@PostMapping("/uploads")
	public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) throws IOException {
		
		// check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }
        
        awsService.storeCsv(file);
        
        S3Object obj = awsService.getCsv("a");
        
        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded the file!");

        return "redirect:/";
	}

}
