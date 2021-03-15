package com.petar.model;

import java.net.URL;
import java.util.UUID;

public class Invoice {
	
	private String id;
	private URL url;
	
	public Invoice(URL url) {
		this.id = UUID.randomUUID().toString();
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	

}
