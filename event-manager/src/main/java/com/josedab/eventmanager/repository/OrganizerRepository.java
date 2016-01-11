package com.josedab.eventmanager.repository;

import com.josedab.eventmanager.domain.Organizer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Organizer entity.
 */
public interface OrganizerRepository extends JpaRepository<Organizer,Long> {

}
