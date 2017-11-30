package com.example.jms.reciever;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

@Component
public class RestClientConnector {

	private static Logger logger = LoggerFactory.getLogger(RestClientConnector.class);
	private Client restClient;
	public RestClientConnector() {
		init();
	}
	private void init() {
		try {
			logger.debug("Creating REST client");
			DefaultClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getClasses().add(JacksonJsonProvider.class);
			clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			restClient = Client.create(clientConfig);
		} catch (Exception e) {
			logger.error("Exception occurred while initializing REST client", e);
		}
	}

	public void processResponseMsg(String msg, String url) {
		WebResource webResource = null;
		try {
			logger.debug("responseurl==" + url);
			webResource = restClient.resource(url);
			webResource.accept(MediaType.APPLICATION_JSON).header("apikey", "ottoigoMhoYeHVG2f8JPAAyH2eYLppkZ")
					.entity(msg, MediaType.APPLICATION_XML_TYPE).post();
		} catch (Exception e) {
			logger.error("Exception occurred while processing Response Message", e);

		}
	}

}
