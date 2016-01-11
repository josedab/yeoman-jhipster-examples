package com.josedab.eventmanager.service;

import com.josedab.eventmanager.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Event.
 */
public interface EventService {

    /**
     * Save a event.
     * @return the persisted entity
     */
    public Event save(Event event);

    /**
     *  get all the events.
     *  @return the list of entities
     */
    public Page<Event> findAll(Pageable pageable);

    /**
     *  get the "id" event.
     *  @return the entity
     */
    public Event findOne(Long id);

    /**
     *  delete the "id" event.
     */
    public void delete(Long id);

    /**
     * search for the event corresponding
     * to the query.
     */
    public List<Event> search(String query);
}
