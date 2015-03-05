package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.repository.AdjectiveRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
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
 * Test class for the AdjectiveResource REST controller.
 *
 * @see AdjectiveResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AdjectiveResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";
    private static final String DEFAULT_GENDER = "SAMPLE_TEXT";
    private static final String UPDATED_GENDER = "UPDATED_TEXT";
    private static final String DEFAULT_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_NUMBER = "UPDATED_TEXT";

    @Inject
    private AdjectiveRepository adjectiveRepository;

    private MockMvc restAdjectiveMockMvc;

    private Adjective adjective;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdjectiveResource adjectiveResource = new AdjectiveResource();
        ReflectionTestUtils.setField(adjectiveResource, "adjectiveRepository", adjectiveRepository);
        this.restAdjectiveMockMvc = MockMvcBuilders.standaloneSetup(adjectiveResource).build();
    }

    @Before
    public void initTest() {
        adjective = new Adjective();
        adjective.setValue(DEFAULT_VALUE);
        adjective.setGender(DEFAULT_GENDER);
        adjective.setNumber(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void createAdjective() throws Exception {
        // Validate the database is empty
        assertThat(adjectiveRepository.findAll()).hasSize(0);

        // Create the Adjective
        restAdjectiveMockMvc.perform(post("/api/adjectives")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adjective)))
                .andExpect(status().isCreated());

        // Validate the Adjective in the database
        List<Adjective> adjectives = adjectiveRepository.findAll();
        assertThat(adjectives).hasSize(1);
        Adjective testAdjective = adjectives.iterator().next();
        assertThat(testAdjective.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testAdjective.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAdjective.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllAdjectives() throws Exception {
        // Initialize the database
        adjectiveRepository.saveAndFlush(adjective);

        // Get all the adjectives
        restAdjectiveMockMvc.perform(get("/api/adjectives"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(adjective.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()))
                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER.toString()))
                .andExpect(jsonPath("$.[0].number").value(DEFAULT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getAdjective() throws Exception {
        // Initialize the database
        adjectiveRepository.saveAndFlush(adjective);

        // Get the adjective
        restAdjectiveMockMvc.perform(get("/api/adjectives/{id}", adjective.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adjective.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdjective() throws Exception {
        // Get the adjective
        restAdjectiveMockMvc.perform(get("/api/adjectives/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdjective() throws Exception {
        // Initialize the database
        adjectiveRepository.saveAndFlush(adjective);

        // Update the adjective
        adjective.setValue(UPDATED_VALUE);
        adjective.setGender(UPDATED_GENDER);
        adjective.setNumber(UPDATED_NUMBER);
        restAdjectiveMockMvc.perform(put("/api/adjectives")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adjective)))
                .andExpect(status().isOk());

        // Validate the Adjective in the database
        List<Adjective> adjectives = adjectiveRepository.findAll();
        assertThat(adjectives).hasSize(1);
        Adjective testAdjective = adjectives.iterator().next();
        assertThat(testAdjective.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testAdjective.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAdjective.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void deleteAdjective() throws Exception {
        // Initialize the database
        adjectiveRepository.saveAndFlush(adjective);

        // Get the adjective
        restAdjectiveMockMvc.perform(delete("/api/adjectives/{id}", adjective.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Adjective> adjectives = adjectiveRepository.findAll();
        assertThat(adjectives).hasSize(0);
    }
}
