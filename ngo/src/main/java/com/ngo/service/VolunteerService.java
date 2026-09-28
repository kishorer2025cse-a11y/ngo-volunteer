package com.ngo.service;

import com.ngo.entity.Event;
import com.ngo.entity.Volunteer;
import com.ngo.repository.EventRepository;
import com.ngo.repository.VolunteerRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final EventRepository eventRepository;

    public VolunteerService(VolunteerRepository volunteerRepository, EventRepository eventRepository) {
        this.volunteerRepository = volunteerRepository;
        this.eventRepository = eventRepository;
    }

    public Volunteer signup(Volunteer v) {
        if (v.getName() == null || v.getName().isBlank() || v.getEmail() == null
                || !v.getEmail().contains("@") || v.getEventId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Valid name, email and event are required.");
        }
        Event event = eventRepository.findById(v.getEventId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found."));
        if (volunteerRepository.countByEventId(event.getId()) >= event.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event capacity is full.");
        }
        v.setAttended(null);
        v.setHours(0);
        return volunteerRepository.save(v);
    }

    public List<Volunteer> getByEvent(Long eventId) {
        return volunteerRepository.findByEventId(eventId);
    }

    public Volunteer markAttendance(Long id, Volunteer data) {
        Volunteer v = volunteerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Volunteer not found."));
        if (data.getHours() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hours cannot be negative.");
        }
        v.setAttended(data.getAttended());
        v.setHours(data.getAttended() != null && data.getAttended() ? data.getHours() : 0);
        return volunteerRepository.save(v);
    }
}
