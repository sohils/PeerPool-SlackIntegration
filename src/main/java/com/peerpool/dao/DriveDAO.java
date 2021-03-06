package com.peerpool.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.peerpool.dao.entity.Destination;
import com.peerpool.dao.entity.Drive;

@Transactional
@Repository
public class DriveDAO {
	@PersistenceContext
	EntityManager em;

	public boolean insertDrive(Drive drive,Set<Destination> via) {
		List<Drive> listDrive = (List<Drive>) em.createQuery("SELECT d from Drive d where d.user_id= :userId AND d.team_name= :teamId")
				.setParameter("userId", drive.getUser_id())
				.setParameter("teamId", drive.getTeam_name())
				.getResultList();
		
		if(listDrive.size()==0){
			for(Destination d : via){
				d.setDrive(drive);
			}
			drive.setVia(via);
			Iterator<Destination> setIterator = via.iterator();
			while(setIterator.hasNext()){
				em.persist(setIterator.next());
			}
			em.persist(drive);
			return true;
		}
		return false;
	}
	
	public void deleteDrive(String user_id, String team_name) {
		List<Drive> listDrive = (List<Drive>) em.createQuery("SELECT d from Drive d where d.user_id= :userId AND d.team_name= :teamId")
				.setParameter("userId", user_id)
				.setParameter("teamId",team_name)
				.getResultList();
		Iterator<Drive> setIterator = listDrive.iterator();
		while(setIterator.hasNext()){
			em.remove(setIterator.next());
		}
	}

	public List<Drive> searchForDrive(Timestamp time, Destination d, String team_id) {
		//List<String> listOfUsers = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		cal.add(Calendar.MINUTE, 30);
		Timestamp later = new Timestamp(cal.getTime().getTime());
		cal.add(Calendar.MINUTE, -60);
		Timestamp before = new Timestamp(cal.getTime().getTime());
		List<Drive> listOfDrives = em.createQuery("SELECT d from Drive d JOIN d.via dest WHERE d.seats>0 AND dest.destination = :drop AND d.time<= :later AND d.time>= :before AND d.team_name= :teamid")
				.setParameter("drop", d.getDestination())
				.setParameter("before", before)
				.setParameter("later", later)
				.setParameter("teamid", team_id)
				.getResultList();
		return listOfDrives;
	}
	
	public List<Timestamp> searchForDriveTimes(Destination d, String team_id) {
		List<Timestamp> listOfTimes= em.createQuery("SELECT d.time from Drive d JOIN d.via dest WHERE d.seats>0 AND dest.destination = :drop AND d.team_name= :teamid")
				.setParameter("drop", d.getDestination())
				.setParameter("teamid", team_id)
				.getResultList();
		return listOfTimes;
	}

	public Drive findByID(String id) {
		return em.find(Drive.class, Long.parseLong(id));
	}

	public void addTime(String user_id, String team_name, Timestamp time) {
		Drive drive = (Drive) em.createQuery("SELECT d from Drive d where d.user_id= :userId AND d.team_name= :teamId")
				.setParameter("userId", user_id)
				.setParameter("teamId",team_name)
				.getSingleResult();
		drive.setTime(time);
		em.flush();
	}

	public void addSeats(String user_id, String team_name, String seats) {
		Drive drive = (Drive) em.createQuery("SELECT d from Drive d where d.user_id= :userId AND d.team_name= :teamId")
				.setParameter("userId", user_id)
				.setParameter("teamId",team_name)
				.getSingleResult();
		drive.setSeats(Integer.parseInt(seats));
		em.flush();
	}
}
