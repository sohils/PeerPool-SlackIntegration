package com.peerpool.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.peerpool.dao.DriveDAO;
import com.peerpool.dao.entity.Destination;
import com.peerpool.dao.entity.Drive;
import com.peerpool.model.ActionInvocation;
import com.peerpool.model.InteractiveAction;
import com.peerpool.model.InteractiveAttachment;
import com.peerpool.model.InteractiveMessage;
import com.peerpool.model.RegisterDriveResponse;
import com.peerpool.model.SlackRequest;

@Component
public class PeerPoolService {

	DriveDAO driveDAO;

	@Autowired
	public PeerPoolService(DriveDAO driveDAO){
		this.driveDAO=driveDAO;
	}

	/**
	 * Registering your drive in the DB.
	 * @param request
	 * @return
	 */
	public RegisterDriveResponse idrive(SlackRequest request) {
		//Extract text
		String text = request.getText();

		//seperate into components
		List<String> parts = Arrays.asList(text.split(";"));
		Drive drive = new Drive();
		Set<Destination> via = new HashSet<Destination>();
		for(int i=0;i < parts.size(); i++) {
			String keyValue[] = parts.get(i).split("=");
			switch(keyValue[0]){
			case "via" :
				String destinations[] = keyValue[1].split(",");
				for(int j=0;j<destinations.length;j++){
					Destination d = new Destination();
					d.setDestination(destinations[j]);
					via.add(d);
				}
				drive.setVia(via);
				break;
			case "to" :
				Destination d = new Destination();
				d.setDestination(keyValue[1]);
				via.add(d);
				break;
			case "at" :
				drive.setTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd ").format(new Date()).concat(keyValue[1])));
				break;
			case "seats":
				drive.setSeats(Integer.parseInt(keyValue[1].substring(0, 1)));
				break;
			}
		}

		//extract username
		drive.setUser_id(request.getUser_id());
		drive.setUser_name(request.getUser_name());
		drive.setTeam_name(request.getTeam_id());

		//persist in DB
		driveDAO.insertDrive(drive,via);

		//return display response
		RegisterDriveResponse response = new RegisterDriveResponse();
		response.setText("Successfully Registered");
		return response;
	}

	public InteractiveMessage needRide(SlackRequest request) {
		//Extract Time, Destination, User
		String text = request.getText();
		Destination d = new Destination();
		Timestamp time = new Timestamp(0);
		//seperate into components
		List<String> parts = Arrays.asList(text.split(";"));
		for(int i=0;i < parts.size(); i++) {
			String keyValue[] = parts.get(i).split("=");
			switch(keyValue[0]){
			case "to" :
				
				d.setDestination(keyValue[1]);
				break;
			case "at" :
				time=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd ").format(new Date()).concat(keyValue[1]));
				break;
			}
		}
		
		
		//Search in DB for the Time and Destination
		List<Drive> drives = driveDAO.searchForDrive(time, d);
		
		//Send back the list of Users for the query.
		InteractiveMessage response = new InteractiveMessage();
		response.setText("Hello there pool-er! Here are your options:");
		List<InteractiveAttachment> attachments = new ArrayList<InteractiveAttachment>();
		List<InteractiveAction> actions =new ArrayList<InteractiveAction>();
		
		for(Drive drive: drives) {
			System.out.println("Possible rides: "+drive.getId());
			InteractiveAction action = new InteractiveAction();
			action.setName("Persone");
			action.setText("<@"+drive.getUser_name()+">");
			action.setType("button");
			action.setValue(String.valueOf(drive.getId()));
			actions.add(action);
		}
		InteractiveAttachment attachment= new InteractiveAttachment();
		attachment.setActions(actions);
		attachment.setCallback_id("1234");
		attachment.setText("Options are:");
		attachment.setFallback("Sorry");
		attachment.setAttachment_type("default");
		attachments.add(attachment);

		//TODO: (Additional) If none available, send him a notif saying that we will inform you once we have a ride available for him
		//      Queue this in an unanswered request list
		//      Notify him via webhook once a ride is available.

		
		response.setAttachments(attachments);
		return response;
	}

	public InteractiveMessage rideWith(ActionInvocation request) {
		//Search for the ride mentioned in the request if still available.

		//If available, set as accepted and send a notification (via webhook) to the Driver and send back drive username and details to the 
		//    guy who booked the ride.

		//If not available, send him a refreshed list by processing the needRide list.


		InteractiveMessage response = new InteractiveMessage();
		response.setText("Hello THere! Your ride with "+request.getActions().get(0).getValue()+" has been reserved. Enjoy the ride!");
		return response;
	}
}
