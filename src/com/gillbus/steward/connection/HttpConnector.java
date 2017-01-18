package com.gillbus.steward.connection;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;

import com.gillbus.steward.Steward;


public class HttpConnector {

	public static final String DEFAULT_CHARSET = "UTF-8";
	private static HttpConnector instance;
	private HttpClient httpClient;
	private PostMethod method;
	private InputStream is;
	
	
	private HttpConnector() {
		
	}
	
	
	public static HttpConnector getInstance() {
		if (instance == null) {
			instance = new HttpConnector();
			instance.newConnect();
		}
		return instance;
	}
	
	
	private void newConnect() {
		httpClient = new HttpClient();
        httpClient.getParams().setContentCharset(DEFAULT_CHARSET);
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        PostMethod method = new PostMethod(Steward.BASE_URL);
        try {
        	if (httpClient.executeMethod(method) != HttpStatus.SC_OK) {
        		instance = null;
        	}
		} catch (IOException e) {
			
		} finally {
			method.releaseConnection();
		}
	}
	
	
	public void newMethod(String uri) {
		method = new PostMethod(uri);
		method.getParams().setContentCharset(DEFAULT_CHARSET);
	}
	
	
	public void addParameter(String id, String value) {
		if (method != null) {
			method.addParameter(id, value);
		}
	}
	
	
	public void executeMethod() {
		try {
            if (httpClient.executeMethod(method) == HttpStatus.SC_OK) {
                is = method.getResponseBodyAsStream();
            } else {
            	is = null;
            }
        } catch (IOException e) {
        	is = null;
        } 
	}


	public InputStream getInputStream() throws IOException {
		return is;
	}
	
	
	public byte[] getBytes() throws IOException {
		ReadableByteChannel channel = Channels.newChannel(is);
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while (channel.read(buffer) != -1) {
			if (buffer.remaining() < 1024) {
				ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() + 1024);
				buffer.flip();
				newBuffer.put(buffer);
				buffer = newBuffer;
			}
		}
		return new String(buffer.array()).trim().getBytes();
	}
	
	
	public void releaseMethod() {
		method.releaseConnection();
	}
}
