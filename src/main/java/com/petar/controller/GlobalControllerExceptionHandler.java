package com.petar.controller;

import java.nio.file.FileSystemException;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.util.NestedServletException;

import com.amazonaws.services.s3.model.AmazonS3Exception;


@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException exception, Model model) { // 
    	model.addAttribute("invalidArgumentError", "");
        return "index.html";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleConstraintViolation(MaxUploadSizeExceededException exception, Model model) { // 
    	model.addAttribute("maxUploadSizeError", "");
    	return "index.html";
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileSystemException.class)
    public String handleConstraintViolation(FileSystemException exception, Model model) { // 
    	model.addAttribute("fileSystemError", "");
    	return "index.html";
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public String handleConstraintViolation(NullPointerException exception, Model model) { // 
    	model.addAttribute("nullPointerError", "");
    	return "index.html";
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AmazonS3Exception.class)
    public String handleConstraintViolation(AmazonS3Exception exception, Model model) { // 
    	model.addAttribute("amazonS3Exception", "");
    	return "index.html";
    }
}