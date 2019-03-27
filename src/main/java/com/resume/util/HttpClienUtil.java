package com.resume.util;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * httpclientπ§æﬂ¿‡
 * @author mac
 *
 */
public class HttpClienUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpClienUtil.class);
	
	private final static String CONTENT_TYPE_TEXT_JSON = "application/json";
    
    public static String postRequest(String url, String param) throws ClientProtocolException, IOException{
        
        CloseableHttpClient client = HttpClients.createDefault();
        logger.info("url"+url);
        logger.info("param"+param);
        
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        
        StringEntity se = new StringEntity(param,ContentType.APPLICATION_JSON);
       
        httpPost.setEntity(se);

        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        logger.info("info"+entity);
        String result="";
        if(entity!= null) {
        	result = EntityUtils.toString(entity, "utf-8");
        	result = URLDecoder.decode(result, "UTF-8");
        
        }
        return result;
    }
}
