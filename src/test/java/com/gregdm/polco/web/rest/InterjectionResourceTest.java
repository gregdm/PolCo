package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.Interjection;
import com.gregdm.polco.repository.InterjectionRepository;

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
 * Test class for the InterjectionResource REST controller.
 *
 * @see InterjectionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InterjectionResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private InterjectionRepository interjectionRepository;

    private MockMvc restInterjectionMockMvc;

    private Interjection interjection;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InterjectionResource interjectionResource = new InterjectionResource();
        ReflectionTestUtils.setField(interjectionResource, "interjectionRepository", interjectionRepository);
        this.restInterjectionMockMvc = MockMvcBuilders.standaloneSetup(interjectionResource).build();
    }

    @Before
    public void initTest() {
        interjection = new Interjection();
        interjection.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createInterjection() throws Exception {
        // Validate the database is empty
        assertThat(interjectionRepository.findAll()).hasSize(0);

        // Create the Interjection
        restInterjectionMockMvc.perform(post("/api/interjections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(interjection)))
                .andExpect(status().isCreated());

        // Validate the Interjection in the database
        List<Interjection> interjections = interjectionRepository.findAll();
        assertThat(interjections).hasSize(1);
        Interjection testInterjection = interjections.iterator().next();
        assertThat(testInterjection.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllInterjections() throws Exception {
        // Initialize the database
        interjectionRepository.saveAndFlush(interjection);

        // Get all the interjections
        restInterjectionMockMvc.perform(get("/api/interjections"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(interjection.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getInterjection() throws Exception {
        // Initialize the database
        interjectionRepository.saveAndFlush(interjection);

        // Get the interjection
        restInterjectionMockMvc.perform(get("/api/interjections/{id}", interjection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(interjection.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInterjection() throws Exception {
        // Get the interjection
        restInterjectionMockMvc.perform(get("/api/interjections/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterjection() throws Exception {
        // Initialize the database
        interjectionRepository.saveAndFlush(interjection);

        // Update the interjection
        interjection.setValue(UPDATED_VALUE);
        restInterjectionMockMvc.perform(put("/api/interjections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(interjection)))
                .andExpect(status().isOk());

        // Validate the Interjection in the database
        List<Interjection> interjections = interjectionRepository.findAll();
        assertThat(interjections).hasSize(1);
        Interjection testInterjection = interjections.iterator().next();
        assertThat(testInterjection.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteInterjection() throws Exception {
        // Initialize the database
        interjectionRepository.saveAndFlush(interjection);

        // Get the interjection
        restInterjectionMockMvc.perform(delete("/api/interjections/{id}", interjection.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Interjection> interjections = interjectionRepository.findAll();
        assertThat(interjections).hasSize(0);
    }
}
