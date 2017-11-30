package com.example.jms.reciever;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import javax.jms.JMSException;
import javax.jms.TextMessage;


@SpringBootApplication
@EnableJms
public class Application implements JmsListenerConfigurer {

	@Autowired
	private ActiveMQConfig config;
	
	@Autowired
	private RestClientConnector connector;

	public static void main(String[] args) {
		// Launch the application
		SpringApplication.run(Application.class);
		System.out.println("***JMS Receiver Application started****");
	}

	@Override
	public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
		// TODO Auto-generated method stub
		for(int i=0;i<config.getName().size();i++) {
			
			String qName=config.getName().get(i);
			String qDesc=config.getDesc().get(i);
			String qUrl=config.getUrl().get(i);
			SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
			endpoint.setId(qDesc);
			endpoint.setDestination(qName);
			endpoint.setMessageListener(message -> {
				String messageBody;
				try {
					 if (message instanceof TextMessage)
				        {
						 messageBody = ((TextMessage) message).getText();
						 System.out.println("QueueName: "+qName+",Recieved message: "+messageBody);
						 connector.processResponseMsg(messageBody, qUrl);
				        }
					 else
						 System.out.println("Message not in text format");
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					System.out.println("Error while reading message from queue: "+qName);
					e.printStackTrace();
				}
			});
			registrar.registerEndpoint(endpoint);
		}
	}

	public DefaultJmsListenerContainerFactory defaultContainerFactory() {
		DefaultJmsListenerContainerFactory defaultContainerFactory = new DefaultJmsListenerContainerFactory();
		return defaultContainerFactory;
	}
}
