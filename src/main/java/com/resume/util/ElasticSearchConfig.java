package com.resume.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

	@Bean(name="client")
	public TransportClient esClint() throws UnknownHostException
	{
		Settings settings = Settings.builder().put("cluster.name","lw").build();
		TransportAddress master=new TransportAddress(InetAddress.getByName("127.0.0.1"),9300);	
		
		TransportClient client =new PreBuiltTransportClient(settings).addTransportAddress(master);
		return client;	
	}


}
