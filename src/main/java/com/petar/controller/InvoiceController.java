package com.petar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petar.service.InvoiceService;

@Controller
public class InvoiceController {
	
	private final InvoiceService invoiceService;
	private final String UPLOAD_DIR = "/home/petar/uploads/";
	
	public InvoiceController(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
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
        
     invoiceService.store(file);
        
     // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded the file!");

        return "redirect:/";
	}

}
