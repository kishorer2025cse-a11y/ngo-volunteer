package com.ngo.service;

import com.ngo.entity.Event;
import com.ngo.repository.EventRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event create(Event e) {
        if (e.getName() == null || e.getName().isBlank() || e.getLocation() == null
                || e.getLocation().isBlank() || e.getDate() == null || e.getCapacity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Name, date, location and capacity (> 0) are required.");
        }
        return eventRepository.save(e);
    }
}
