package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Meeting findById(Long id) {
		return connector.getSession().get(Meeting.class, id);
	}

	public void update(Meeting meeting) {
		Session session = connector.getSession();
		Transaction transaction = session.beginTransaction();
		connector.getSession().update(meeting);
		transaction.commit();
	}
}
