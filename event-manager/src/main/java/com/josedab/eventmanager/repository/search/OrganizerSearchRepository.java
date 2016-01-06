package com.josedab.eventmanager.repository.search;

import com.josedab.eventmanager.domain.Organizer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Organizer entity.
 */
public interface OrganizerSearchRepository extends ElasticsearchRepository<Organizer, Long> {
}
