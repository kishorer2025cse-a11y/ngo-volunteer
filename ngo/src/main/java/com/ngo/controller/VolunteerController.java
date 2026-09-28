package com.ngo.controller;

import com.ngo.entity.Volunteer;
import com.ngo.service.VolunteerService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping("/signup")
    public Volunteer signup(@RequestBody Volunteer v) {
        return volunteerService.signup(v);
    }

    @GetMapping("/event/{eventId}")
    public List<Volunteer> getByEvent(@PathVariable Long eventId) {
        return volunteerService.getByEvent(eventId);
    }

    @PutMapping("/{id}/attendance")
    public Volunteer attendance(@PathVariable Long id, @RequestBody Volunteer data) {
        return volunteerService.markAttendance(id, data);
    }
}
