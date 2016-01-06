package com.josedab.eventmanager.web.rest;

import com.josedab.eventmanager.Application;
import com.josedab.eventmanager.domain.Organizer;
import com.josedab.eventmanager.repository.OrganizerRepository;
import com.josedab.eventmanager.service.OrganizerService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the OrganizerResource REST controller.
 *
 * @see OrganizerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrganizerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private OrganizerRepository organizerRepository;

    @Inject
    private OrganizerService organizerService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrganizerMockMvc;

    private Organizer organizer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrganizerResource organizerResource = new OrganizerResource();
        ReflectionTestUtils.setField(organizerResource, "organizerService", organizerService);
        this.restOrganizerMockMvc = MockMvcBuilders.standaloneSetup(organizerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        organizer = new Organizer();
        organizer.setName(DEFAULT_NAME);
        organizer.setTitle(DEFAULT_TITLE);
        organizer.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createOrganizer() throws Exception {
        int databaseSizeBeforeCreate = organizerRepository.findAll().size();

        // Create the Organizer

        restOrganizerMockMvc.perform(post("/api/organizers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizer)))
                .andExpect(status().isCreated());

        // Validate the Organizer in the database
        List<Organizer> organizers = organizerRepository.findAll();
        assertThat(organizers).hasSize(databaseSizeBeforeCreate + 1);
        Organizer testOrganizer = organizers.get(organizers.size() - 1);
        assertThat(testOrganizer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganizer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOrganizer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizerRepository.findAll().size();
        // set the field null
        organizer.setName(null);

        // Create the Organizer, which fails.

        restOrganizerMockMvc.perform(post("/api/organizers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizer)))
                .andExpect(status().isBadRequest());

        List<Organizer> organizers = organizerRepository.findAll();
        assertThat(organizers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganizers() throws Exception {
        // Initialize the database
        organizerRepository.saveAndFlush(organizer);

        // Get all the organizers
        restOrganizerMockMvc.perform(get("/api/organizers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(organizer.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getOrganizer() throws Exception {
        // Initialize the database
        organizerRepository.saveAndFlush(organizer);

        // Get the organizer
        restOrganizerMockMvc.perform(get("/api/organizers/{id}", organizer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(organizer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizer() throws Exception {
        // Get the organizer
        restOrganizerMockMvc.perform(get("/api/organizers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizer() throws Exception {
        // Initialize the database
        organizerRepository.saveAndFlush(organizer);

		int databaseSizeBeforeUpdate = organizerRepository.findAll().size();

        // Update the organizer
        organizer.setName(UPDATED_NAME);
        organizer.setTitle(UPDATED_TITLE);
        organizer.setDescription(UPDATED_DESCRIPTION);

        restOrganizerMockMvc.perform(put("/api/organizers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizer)))
                .andExpect(status().isOk());

        // Validate the Organizer in the database
        List<Organizer> organizers = organizerRepository.findAll();
        assertThat(organizers).hasSize(databaseSizeBeforeUpdate);
        Organizer testOrganizer = organizers.get(organizers.size() - 1);
        assertThat(testOrganizer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganizer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOrganizer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteOrganizer() throws Exception {
        // Initialize the database
        organizerRepository.saveAndFlush(organizer);

		int databaseSizeBeforeDelete = organizerRepository.findAll().size();

        // Get the organizer
        restOrganizerMockMvc.perform(delete("/api/organizers/{id}", organizer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Organizer> organizers = organizerRepository.findAll();
        assertThat(organizers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
