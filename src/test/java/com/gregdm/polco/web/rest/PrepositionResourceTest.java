package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.Preposition;
import com.gregdm.polco.repository.PrepositionRepository;

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
 * Test class for the PrepositionResource REST controller.
 *
 * @see PrepositionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrepositionResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private PrepositionRepository prepositionRepository;

    private MockMvc restPrepositionMockMvc;

    private Preposition preposition;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrepositionResource prepositionResource = new PrepositionResource();
        ReflectionTestUtils.setField(prepositionResource, "prepositionRepository", prepositionRepository);
        this.restPrepositionMockMvc = MockMvcBuilders.standaloneSetup(prepositionResource).build();
    }

    @Before
    public void initTest() {
        preposition = new Preposition();
        preposition.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createPreposition() throws Exception {
        // Validate the database is empty
        assertThat(prepositionRepository.findAll()).hasSize(0);

        // Create the Preposition
        restPrepositionMockMvc.perform(post("/api/prepositions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preposition)))
                .andExpect(status().isCreated());

        // Validate the Preposition in the database
        List<Preposition> prepositions = prepositionRepository.findAll();
        assertThat(prepositions).hasSize(1);
        Preposition testPreposition = prepositions.iterator().next();
        assertThat(testPreposition.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllPrepositions() throws Exception {
        // Initialize the database
        prepositionRepository.saveAndFlush(preposition);

        // Get all the prepositions
        restPrepositionMockMvc.perform(get("/api/prepositions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(preposition.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getPreposition() throws Exception {
        // Initialize the database
        prepositionRepository.saveAndFlush(preposition);

        // Get the preposition
        restPrepositionMockMvc.perform(get("/api/prepositions/{id}", preposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(preposition.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPreposition() throws Exception {
        // Get the preposition
        restPrepositionMockMvc.perform(get("/api/prepositions/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreposition() throws Exception {
        // Initialize the database
        prepositionRepository.saveAndFlush(preposition);

        // Update the preposition
        preposition.setValue(UPDATED_VALUE);
        restPrepositionMockMvc.perform(put("/api/prepositions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(preposition)))
                .andExpect(status().isOk());

        // Validate the Preposition in the database
        List<Preposition> prepositions = prepositionRepository.findAll();
        assertThat(prepositions).hasSize(1);
        Preposition testPreposition = prepositions.iterator().next();
        assertThat(testPreposition.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deletePreposition() throws Exception {
        // Initialize the database
        prepositionRepository.saveAndFlush(preposition);

        // Get the preposition
        restPrepositionMockMvc.perform(delete("/api/prepositions/{id}", preposition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Preposition> prepositions = prepositionRepository.findAll();
        assertThat(prepositions).hasSize(0);
    }
}
