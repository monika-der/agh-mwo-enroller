package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipantsSorted(@RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder, @RequestParam(required = false) String key) {
		Collection<Participant> participants;
		if (sortBy != null && sortOrder != null) {
			if (!sortBy.equals("login") || !sortOrder.equalsIgnoreCase("asc") && !sortOrder.equalsIgnoreCase("desc")) {
				return new ResponseEntity<String>("Invalid sort parameter.", HttpStatus.BAD_REQUEST);
			}
			participants = participantService.getAllSorted(sortBy, sortOrder);
		} else if (key != null) {
			participants = participantService.getParticipantByKey(key);
		} else {
			participants = participantService.getAll();
		}
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		if (participantService.findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity<String>("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}
		participantService.add(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<String>("Unable to delete. User doesn't exist.", HttpStatus.NOT_FOUND);
		}
		participantService.delete(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant updatedParticipant) {
		Participant existingParticipant = participantService.findByLogin(login);
		if (existingParticipant == null) {
			return new ResponseEntity<String>("Unable to update. User doesn't exist.", HttpStatus.NOT_FOUND);
		}
		existingParticipant.setPassword(updatedParticipant.getPassword());
		existingParticipant.setLogin(updatedParticipant.getLogin());
		participantService.update(existingParticipant);
		return new ResponseEntity<Participant>(existingParticipant, HttpStatus.OK);
	}
}
