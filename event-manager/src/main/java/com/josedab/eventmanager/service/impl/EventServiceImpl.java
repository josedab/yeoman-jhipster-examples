package com.josedab.eventmanager.service.impl;

import com.josedab.eventmanager.service.EventService;
import com.josedab.eventmanager.domain.Event;
import com.josedab.eventmanager.repository.EventRepository;
import com.josedab.eventmanager.repository.search.EventSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Event.
 */
@Service
@Transactional
public class EventServiceImpl implements EventService{

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);
    
    @Inject
    private EventRepository eventRepository;
    
    @Inject
    private EventSearchRepository eventSearchRepository;
    
    /**
     * Save a event.
     * @return the persisted entity
     */
    public Event save(Event event) {
        log.debug("Request to save Event : {}", event);
        Event result = eventRepository.save(event);
        eventSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the events.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Event> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        Page<Event> result = eventRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one event by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Event findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        Event event = eventRepository.findOne(id);
        return event;
    }

    /**
     *  delete the  event by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.delete(id);
        eventSearchRepository.delete(id);
    }

    /**
     * search for the event corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Event> search(String query) {
        
        log.debug("REST request to search Events for query {}", query);
        return StreamSupport
            .stream(eventSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
