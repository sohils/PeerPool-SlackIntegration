package com.peerpool;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
		
		
		InteractiveMessage response = new InteractiveMessage();
		response.setText("Hello there. Thank you for registering your drive. We will get back to you shortly with the conformtion of registration!");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(
			value="/canceldrive", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> cancelDrive(SlackRequest request) {
		//RegisterDriveResponse response = new RegisterDriveResponse();
		//response.setText("Hello there. Thank you for registering your drive: <@" + reqeust.getUser_id()+">");
		return new ResponseEntity<>(service.cancelDrive(request), HttpStatus.OK);
	}

	@RequestMapping(
			value="/choosedrive", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> chooseDrive(SlackRequest request) {  
		System.out.println(request.getChannel_name());
		return new ResponseEntity<>(service.needRide(request), HttpStatus.OK);
	}

	@RequestMapping(
			value="/ridewith", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> rideWith(ActionInvocationPayload request) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		ActionInvocation actionRequest = mapper.readValue(request.getPayload(), ActionInvocation.class);

		if(actionRequest.getCallback_id().equals("setTime")){
			return new ResponseEntity<>(service.addTimeDetails(actionRequest), HttpStatus.OK);
		} if(actionRequest.getCallback_id().equals("setSeats")) {
			return new ResponseEntity<>(service.addSeatDetails(actionRequest), HttpStatus.OK);
		}
		return new ResponseEntity<>(service.rideWith(actionRequest), HttpStatus.OK);
	}

}
