package com.ngo.repository;

import com.ngo.entity.Volunteer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    List<Volunteer> findByEventId(Long eventId);
    long countByEventId(Long eventId);
}
