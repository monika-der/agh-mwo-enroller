package com.company.enroller.persistence;

import java.util.Collection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Participant findByLogin(String login) {
		return connector.getSession().get(Participant.class, login);
	}

	public void add(Participant participant) {
		Session session = connector.getSession();
		Transaction transaction = session.beginTransaction();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(participant.getPassword());
		participant.setPassword(hashedPassword);
		connector.getSession().save(participant);
		transaction.commit();
	}

	public void delete(Participant participant) {
		Session session = connector.getSession();
		Transaction transaction = session.beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}

	public Collection<Participant> getAllSorted(String sortBy, String sortOrder) {
		String hql = "FROM Participant ORDER BY " + sortBy + " " + sortOrder;
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Collection<Participant> getParticipantByKey(String key) {
		String hql = "FROM Participant WHERE login LIKE" + " '%" + key + "%'";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Participant update(Participant participant) {
		Session session = connector.getSession();
		Transaction transaction = session.beginTransaction();
		connector.getSession().update(participant);
		return participant;
	}
}
