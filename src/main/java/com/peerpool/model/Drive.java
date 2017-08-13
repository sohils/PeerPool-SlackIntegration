package com.peerpool.model;

import java.sql.Timestamp;
import java.util.List;

public class Drive {
	private long id;
	private String userid;
	private String user_name;
	private String team_name;
	private Timestamp time;
	private Integer seats;
	private Destination destination;
	private List<Destination> via;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public Integer getSeats() {
		return seats;
	}
	public void setSeats(Integer seats) {
		this.seats = seats;
	}
	public List<Destination> getVia() {
		return via;
	}
	public void setVia(List<Destination> via) {
		this.via = via;
	}
	public Destination getDestination() {
		return destination;
	}
	public void setDestination(Destination destination) {
		this.destination = destination;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getTeam_name() {
		return team_name;
	}
	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}
}
