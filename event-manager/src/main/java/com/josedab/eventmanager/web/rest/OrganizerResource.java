package com.josedab.eventmanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.josedab.eventmanager.domain.Organizer;
import com.josedab.eventmanager.service.OrganizerService;
import com.josedab.eventmanager.web.rest.util.HeaderUtil;
import com.josedab.eventmanager.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Organizer.
 */
@RestController
@RequestMapping("/api")
public class OrganizerResource {

    private final Logger log = LoggerFactory.getLogger(OrganizerResource.class);
        
    @Inject
    private OrganizerService organizerService;
    
    /**
     * POST  /organizers -> Create a new organizer.
     */
    @RequestMapping(value = "/organizers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organizer> createOrganizer(@Valid @RequestBody Organizer organizer) throws URISyntaxException {
        log.debug("REST request to save Organizer : {}", organizer);
        if (organizer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("organizer", "idexists", "A new organizer cannot already have an ID")).body(null);
        }
        Organizer result = organizerService.save(organizer);
        return ResponseEntity.created(new URI("/api/organizers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("organizer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /organizers -> Updates an existing organizer.
     */
    @RequestMapping(value = "/organizers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organizer> updateOrganizer(@Valid @RequestBody Organizer organizer) throws URISyntaxException {
        log.debug("REST request to update Organizer : {}", organizer);
        if (organizer.getId() == null) {
            return createOrganizer(organizer);
        }
        Organizer result = organizerService.save(organizer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("organizer", organizer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /organizers -> get all the organizers.
     */
    @RequestMapping(value = "/organizers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Organizer>> getAllOrganizers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Organizers");
        Page<Organizer> page = organizerService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /organizers/:id -> get the "id" organizer.
     */
    @RequestMapping(value = "/organizers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organizer> getOrganizer(@PathVariable Long id) {
        log.debug("REST request to get Organizer : {}", id);
        Organizer organizer = organizerService.findOne(id);
        return Optional.ofNullable(organizer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /organizers/:id -> delete the "id" organizer.
     */
    @RequestMapping(value = "/organizers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrganizer(@PathVariable Long id) {
        log.debug("REST request to delete Organizer : {}", id);
        organizerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("organizer", id.toString())).build();
    }

    /**
     * SEARCH  /_search/organizers/:query -> search for the organizer corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/organizers/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Organizer> searchOrganizers(@PathVariable String query) {
        log.debug("Request to search Organizers for query {}", query);
        return organizerService.search(query);
    }
}
