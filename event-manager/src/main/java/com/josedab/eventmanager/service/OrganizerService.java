package com.josedab.eventmanager.service;

import com.josedab.eventmanager.domain.Organizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Organizer.
 */
public interface OrganizerService {

    /**
     * Save a organizer.
     * @return the persisted entity
     */
    public Organizer save(Organizer organizer);

    /**
     *  get all the organizers.
     *  @return the list of entities
     */
    public Page<Organizer> findAll(Pageable pageable);

    /**
     *  get the "id" organizer.
     *  @return the entity
     */
    public Organizer findOne(Long id);

    /**
     *  delete the "id" organizer.
     */
    public void delete(Long id);

    /**
     * search for the organizer corresponding
     * to the query.
     */
    public List<Organizer> search(String query);
}
