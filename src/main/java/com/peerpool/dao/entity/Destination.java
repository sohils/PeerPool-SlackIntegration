package com.peerpool.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Destination {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String destination;
	
	@ManyToOne
	Drive drive;

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Drive getDrive() {
		return drive;
	}

	public void setDrive(Drive drive) {
		this.drive = drive;
	}
	
	 @Override
	    public int hashCode() {
	        int result = 17;
	        result = 31 * result + destination.hashCode();
	        result = (int) (31 * result + id);
	        return result;
	    }
}
