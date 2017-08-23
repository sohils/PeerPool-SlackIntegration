package com.peerpool.client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class HTTPRequestClient {
	public void sendPost(String url, String jsonBody) throws ClientProtocolException, IOException{
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		
		StringEntity entity= new StringEntity(jsonBody);
		
		post.setHeader("Content-type", "application/json");
		post.setEntity(entity);
		
		HttpResponse response = client.execute(post);
	}
}
