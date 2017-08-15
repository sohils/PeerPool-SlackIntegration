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
import com.peerpool.model.Option;
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
	public InteractiveMessage idrive(SlackRequest request) {
		//Extract text
		String text = request.getText();

		//seperate into components
		List<String> parts = Arrays.asList(text.split(" via "));
		Drive drive = new Drive();
		Set<Destination> via = new HashSet<Destination>();
		Destination d = new Destination();
		d.setDestination(parts.get(0).toLowerCase());
		via.add(d);
		String listOfVia[] = parts.get(1).split(",");
		for(int j=0;j<listOfVia.length;j++){
			Destination d1 = new Destination();
			d1.setDestination(listOfVia[j].toLowerCase());
			via.add(d1);
		}

		drive.setVia(via);


		//extract username
		drive.setUser_id(request.getUser_id());
		drive.setUser_name(request.getUser_name());
		drive.setTeam_name(request.getTeam_id());

		//persist in DB
		driveDAO.insertDrive(drive,via);

		//return display response
		InteractiveMessage response = new InteractiveMessage();
		response.setText("Around what time are you expected to leave?");
		InteractiveAttachment attachment= new InteractiveAttachment();
		attachment.setText("Lets say around ");
		attachment.setFallback("Something is worng!");
		attachment.setCallback_id("setTime");
		InteractiveAction action = new InteractiveAction();
		action.setName("time_list");
		action.setType("select");
		List<Option> listOfTimes = new ArrayList<Option>();
		for(int i=5; i<10; i++){
			listOfTimes.add(new Option(Integer.toString(i)+" PM",Integer.toString(i+12)+":00:00"));
		}
		action.setOptions(listOfTimes);
		attachment.setActions(new ArrayList<InteractiveAction>());
		attachment.getActions().add(action);
		response.setAttachments(new ArrayList<InteractiveAttachment>());
		response.getAttachments().add(attachment);
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

		String team_id=request.getTeam_id();

		//Search in DB for the Time and Destination
		List<Drive> drives = driveDAO.searchForDrive(time, d, team_id);

		//Send back the list of Users for the query.
		InteractiveMessage response = new InteractiveMessage();
		response.setText("Hello there pool-er! Here are your options:");
		List<InteractiveAttachment> attachments = new ArrayList<InteractiveAttachment>();
		List<InteractiveAction> actions =new ArrayList<InteractiveAction>();

		for(Drive drive: drives) {
			System.out.println("Possible rides: "+drive.getId());
			InteractiveAction action = new InteractiveAction();
			action.setName("Persone");
			action.setText(drive.getUser_name());
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

	public InteractiveMessage addTimeDetails(ActionInvocation request) {
		Timestamp time=Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd ").format(new Date()).concat(request.getActions().get(0).getSelected_options().get(0).getValue()));
		String user_id = request.getUser().getId();
		String team_id = request.getTeam().getId();
		driveDAO.addTime(user_id,team_id,time);
		
		InteractiveMessage response = new InteractiveMessage();
		response.setText("How many seats do yo u have to spare?");
		InteractiveAttachment attachment= new InteractiveAttachment();
		attachment.setText("About: ");
		attachment.setFallback("Something is worng!");
		attachment.setCallback_id("setSeats");
		InteractiveAction action = new InteractiveAction();
		action.setName("seats_list");
		action.setType("select");
		List<Option> listOfSeats = new ArrayList<Option>();
		for(int i=1; i<6; i++){
			listOfSeats.add(new Option(Integer.toString(i)+" seats",Integer.toString(i)));
		}
		action.setOptions(listOfSeats);
		attachment.setActions(new ArrayList<InteractiveAction>());
		attachment.getActions().add(action);
		response.setAttachments(new ArrayList<InteractiveAttachment>());
		response.getAttachments().add(attachment);
		return response;
	}

	public InteractiveMessage rideWith(ActionInvocation request) {
		//Search for the ride mentioned in the request if still available.
		Drive drive = driveDAO.findByID(request.getActions().get(0).getValue());

		//If available, set as accepted and send a notification (via webhook) to the Driver and send back drive username and details to the 
		//    guy who booked the ride.

		//If not available, send him a refreshed list by processing the needRide list.


		InteractiveMessage response = new InteractiveMessage();
		response.setText("Hello there! Your ride with <@"+drive.getUser_id()+"> has been reserved. Enjoy the ride!");
		response.setReplace_original(true);
		return response;
	}
}
