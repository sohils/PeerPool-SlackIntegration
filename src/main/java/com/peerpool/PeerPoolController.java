package com.peerpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<RegisterDriveResponse> registerDrive(SlackRequest request) {
		//RegisterDriveResponse response = new RegisterDriveResponse();
		//response.setText("Hello there. Thank you for registering your drive: <@" + reqeust.getUser_id()+">");
		return new ResponseEntity<RegisterDriveResponse>(service.idrive(request), HttpStatus.OK);
    }
    
    @RequestMapping(
			value="/choosedrive", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<InteractiveMessage> chooseDrive(SlackRequest request) {    	
    	return new ResponseEntity<InteractiveMessage>(service.needRide(request), HttpStatus.OK);
    }
    
    @RequestMapping(
			value="/ridewith", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisterDriveResponse> rideWith() {
		RegisterDriveResponse response = new RegisterDriveResponse();
		response.setText("Hello there. Thank you for booking your drive: ");
		return new ResponseEntity<RegisterDriveResponse>(response, HttpStatus.OK);
    }
    
}
