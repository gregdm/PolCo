package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.NominalDet;
import com.gregdm.polco.repository.NominalDetRepository;

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
 * Test class for the NominalDetResource REST controller.
 *
 * @see NominalDetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NominalDetResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private NominalDetRepository nominalDetRepository;

    private MockMvc restNominalDetMockMvc;

    private NominalDet nominalDet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NominalDetResource nominalDetResource = new NominalDetResource();
        ReflectionTestUtils.setField(nominalDetResource, "nominalDetRepository", nominalDetRepository);
        this.restNominalDetMockMvc = MockMvcBuilders.standaloneSetup(nominalDetResource).build();
    }

    @Before
    public void initTest() {
        nominalDet = new NominalDet();
        nominalDet.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createNominalDet() throws Exception {
        // Validate the database is empty
        assertThat(nominalDetRepository.findAll()).hasSize(0);

        // Create the NominalDet
        restNominalDetMockMvc.perform(post("/api/nominalDets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nominalDet)))
                .andExpect(status().isCreated());

        // Validate the NominalDet in the database
        List<NominalDet> nominalDets = nominalDetRepository.findAll();
        assertThat(nominalDets).hasSize(1);
        NominalDet testNominalDet = nominalDets.iterator().next();
        assertThat(testNominalDet.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllNominalDets() throws Exception {
        // Initialize the database
        nominalDetRepository.saveAndFlush(nominalDet);

        // Get all the nominalDets
        restNominalDetMockMvc.perform(get("/api/nominalDets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(nominalDet.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNominalDet() throws Exception {
        // Initialize the database
        nominalDetRepository.saveAndFlush(nominalDet);

        // Get the nominalDet
        restNominalDetMockMvc.perform(get("/api/nominalDets/{id}", nominalDet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(nominalDet.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNominalDet() throws Exception {
        // Get the nominalDet
        restNominalDetMockMvc.perform(get("/api/nominalDets/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNominalDet() throws Exception {
        // Initialize the database
        nominalDetRepository.saveAndFlush(nominalDet);

        // Update the nominalDet
        nominalDet.setValue(UPDATED_VALUE);
        restNominalDetMockMvc.perform(put("/api/nominalDets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nominalDet)))
                .andExpect(status().isOk());

        // Validate the NominalDet in the database
        List<NominalDet> nominalDets = nominalDetRepository.findAll();
        assertThat(nominalDets).hasSize(1);
        NominalDet testNominalDet = nominalDets.iterator().next();
        assertThat(testNominalDet.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteNominalDet() throws Exception {
        // Initialize the database
        nominalDetRepository.saveAndFlush(nominalDet);

        // Get the nominalDet
        restNominalDetMockMvc.perform(delete("/api/nominalDets/{id}", nominalDet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NominalDet> nominalDets = nominalDetRepository.findAll();
        assertThat(nominalDets).hasSize(0);
    }
}
