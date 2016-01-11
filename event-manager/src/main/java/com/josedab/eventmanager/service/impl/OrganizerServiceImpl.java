package com.josedab.eventmanager.service.impl;

import com.josedab.eventmanager.service.OrganizerService;
import com.josedab.eventmanager.domain.Organizer;
import com.josedab.eventmanager.repository.OrganizerRepository;
import com.josedab.eventmanager.repository.search.OrganizerSearchRepository;
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
 * Service Implementation for managing Organizer.
 */
@Service
@Transactional
public class OrganizerServiceImpl implements OrganizerService{

    private final Logger log = LoggerFactory.getLogger(OrganizerServiceImpl.class);
    
    @Inject
    private OrganizerRepository organizerRepository;
    
    @Inject
    private OrganizerSearchRepository organizerSearchRepository;
    
    /**
     * Save a organizer.
     * @return the persisted entity
     */
    public Organizer save(Organizer organizer) {
        log.debug("Request to save Organizer : {}", organizer);
        Organizer result = organizerRepository.save(organizer);
        organizerSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the organizers.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Organizer> findAll(Pageable pageable) {
        log.debug("Request to get all Organizers");
        Page<Organizer> result = organizerRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one organizer by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Organizer findOne(Long id) {
        log.debug("Request to get Organizer : {}", id);
        Organizer organizer = organizerRepository.findOne(id);
        return organizer;
    }

    /**
     *  delete the  organizer by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Organizer : {}", id);
        organizerRepository.delete(id);
        organizerSearchRepository.delete(id);
    }

    /**
     * search for the organizer corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Organizer> search(String query) {
        
        log.debug("REST request to search Organizers for query {}", query);
        return StreamSupport
            .stream(organizerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
