package com.peerpool.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.peerpool.model.Destination;
import com.peerpool.model.Drive;
import com.peerpool.model.InteractiveAction;
import com.peerpool.model.InteractiveAttachment;
import com.peerpool.model.InteractiveMessage;
import com.peerpool.model.RegisterDriveResponse;
import com.peerpool.model.SlackRequest;

@Component
public class PeerPoolService {
	
	/**
	 * Registering your drive in the DB.
	 * @param request
	 * @return
	 */
	public RegisterDriveResponse idrive(SlackRequest request) {
		//Extract text
		String text = request.getText();
		
		//seperate into components
		List<String> parts = Arrays.asList(text.split("\\s*,\\s*"));
		Drive drive = new Drive();
		List<Destination> via = new ArrayList<Destination>();
		for(int i=0;i < parts.size(); i++) {
			String keyValue[] = parts.get(i).split("=");
			switch(keyValue[0]){
			case "via" :
				String destinations[] = keyValue[1].split(",");
				for(int j=0;j<destinations.length;j++)
					via.add(new Destination(destinations[j]));
				drive.setVia(via);
				break;
			case "to" :
				drive.setDestination(new Destination(keyValue[1]));
				break;
			case "at" :
				drive.setTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd ").format(new Date()).concat(keyValue[1])));
				break;
			case "seats":
				drive.setSeats(Integer.parseInt(keyValue[1]));
				break;
			}
		}
		
		//extract username
		drive.setUserid(request.getUser_id());
		
		//persist in DB
		
		
		//return display response
		RegisterDriveResponse response = new RegisterDriveResponse();
		response.setText("Successfully Registered");
		return response;
	}
	
	public InteractiveMessage needRide(SlackRequest request) {
		InteractiveMessage response = new InteractiveMessage();
    	response.setText("Hello THere! Here are your options:");
    	List<InteractiveAttachment> attachments = new ArrayList<InteractiveAttachment>();
    	InteractiveAction action1 = new InteractiveAction();
    	action1.setName("person");
    	action1.setText("Person1");
    	action1.setType("button");
    	action1.setValue("p1");
    	InteractiveAction action2 = new InteractiveAction();
    	action2.setName("person");
    	action2.setText("Person2");
    	action2.setType("button");
    	action2.setValue("p2");
    	List<InteractiveAction> actions =new ArrayList<InteractiveAction>();
    	actions.add(action1);
    	actions.add(action2);
    	InteractiveAttachment attachment= new InteractiveAttachment();
    	attachment.setActions(actions);
    	attachment.setCallback_id("1234");
    	attachment.setText("Options");
    	attachment.setFallback("SOrry");
    	attachments.add(attachment);
    	response.setAttachments(attachments);
		return response;
	}
}
