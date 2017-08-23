package com.peerpool;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peerpool.model.ActionInvocation;
import com.peerpool.model.ActionInvocationPayload;
import com.peerpool.model.InteractiveMessage;
import com.peerpool.model.SlackRequest;
import com.peerpool.service.PeerPoolService;

@RestController
public class PeerPoolController {

	@Autowired PeerPoolService service;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping(
			value="/idrive", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registerDrive(SlackRequest request) {
		try {
			service.idrive(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return replyWith("Hello there. Thank you for registering your drive. We will get back to you shortly with the conformtion of registration!");
	}
	
	@RequestMapping(
			value="/canceldrive", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> cancelDrive(SlackRequest request) throws ClientProtocolException, IOException {
		service.cancelDrive(request);
		return replyWith("Hang on a sec...");
	}

	@RequestMapping(
			value="/choosedrive", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> chooseDrive(SlackRequest request) throws ClientProtocolException, IOException {  
		service.needRide(request);
		return replyWith("Seraching...");
	}

	@RequestMapping(
			value="/ridewith", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> rideWith(ActionInvocationPayload request) throws IOException {
		doAction(request);
		return replyWith(null);
	}
	
	@Async
	public void doAction(ActionInvocationPayload request) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ActionInvocation actionRequest = mapper.readValue(request.getPayload(), ActionInvocation.class);

		switch(actionRequest.getCallback_id()){
		case "setTime":
			service.addTimeDetails(actionRequest);
			break;
		case "setSeats":
			service.addSeatDetails(actionRequest);	
			break;
		default:
			service.rideWith(actionRequest);
		}
	}
	
	private ResponseEntity<Object> replyWith(String text) {
		InteractiveMessage response = new InteractiveMessage();
		response.setText(text);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
