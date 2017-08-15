package com.peerpool;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peerpool.model.ActionInvocation;
import com.peerpool.model.ActionInvocationPayload;
import com.peerpool.model.InteractiveMessage;
import com.peerpool.model.RegisterDriveResponse;
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
	public ResponseEntity<InteractiveMessage> registerDrive(SlackRequest request) {
		//RegisterDriveResponse response = new RegisterDriveResponse();
		//response.setText("Hello there. Thank you for registering your drive: <@" + reqeust.getUser_id()+">");
		return new ResponseEntity<InteractiveMessage>(service.idrive(request), HttpStatus.OK);
	}

	@RequestMapping(
			value="/choosedrive", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InteractiveMessage> chooseDrive(SlackRequest request) {  
		System.out.println(request.getChannel_name());
		return new ResponseEntity<InteractiveMessage>(service.needRide(request), HttpStatus.OK);
	}

	@RequestMapping(
			value="/ridewith", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InteractiveMessage> rideWith(ActionInvocationPayload request) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		ActionInvocation actionRequest = mapper.readValue(request.getPayload(), ActionInvocation.class);

		if(actionRequest.getCallback_id().equals("setTime")){
			return new ResponseEntity<InteractiveMessage>(service.addTimeDetails(actionRequest), HttpStatus.OK);
		} if(actionRequest.getCallback_id().equals("setSeats")) {
			return new ResponseEntity<InteractiveMessage>(service.addSeatDetails(actionRequest), HttpStatus.OK);
		}
		return new ResponseEntity<InteractiveMessage>(service.rideWith(actionRequest), HttpStatus.OK);
	}

}
