package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.NominalDetTrans;
import com.gregdm.polco.repository.NominalDetTransRepository;

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
 * Test class for the NominalDetTransResource REST controller.
 *
 * @see NominalDetTransResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NominalDetTransResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private NominalDetTransRepository nominalDetTransRepository;

    private MockMvc restNominalDetTransMockMvc;

    private NominalDetTrans nominalDetTrans;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NominalDetTransResource nominalDetTransResource = new NominalDetTransResource();
        ReflectionTestUtils.setField(nominalDetTransResource, "nominalDetTransRepository", nominalDetTransRepository);
        this.restNominalDetTransMockMvc = MockMvcBuilders.standaloneSetup(nominalDetTransResource).build();
    }

    @Before
    public void initTest() {
        nominalDetTrans = new NominalDetTrans();
        nominalDetTrans.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createNominalDetTrans() throws Exception {
        // Validate the database is empty
        assertThat(nominalDetTransRepository.findAll()).hasSize(0);

        // Create the NominalDetTrans
        restNominalDetTransMockMvc.perform(post("/api/nominalDetTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nominalDetTrans)))
                .andExpect(status().isCreated());

        // Validate the NominalDetTrans in the database
        List<NominalDetTrans> nominalDetTranss = nominalDetTransRepository.findAll();
        assertThat(nominalDetTranss).hasSize(1);
        NominalDetTrans testNominalDetTrans = nominalDetTranss.iterator().next();
        assertThat(testNominalDetTrans.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllNominalDetTranss() throws Exception {
        // Initialize the database
        nominalDetTransRepository.saveAndFlush(nominalDetTrans);

        // Get all the nominalDetTranss
        restNominalDetTransMockMvc.perform(get("/api/nominalDetTranss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(nominalDetTrans.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNominalDetTrans() throws Exception {
        // Initialize the database
        nominalDetTransRepository.saveAndFlush(nominalDetTrans);

        // Get the nominalDetTrans
        restNominalDetTransMockMvc.perform(get("/api/nominalDetTranss/{id}", nominalDetTrans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(nominalDetTrans.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNominalDetTrans() throws Exception {
        // Get the nominalDetTrans
        restNominalDetTransMockMvc.perform(get("/api/nominalDetTranss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNominalDetTrans() throws Exception {
        // Initialize the database
        nominalDetTransRepository.saveAndFlush(nominalDetTrans);

        // Update the nominalDetTrans
        nominalDetTrans.setValue(UPDATED_VALUE);
        restNominalDetTransMockMvc.perform(put("/api/nominalDetTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nominalDetTrans)))
                .andExpect(status().isOk());

        // Validate the NominalDetTrans in the database
        List<NominalDetTrans> nominalDetTranss = nominalDetTransRepository.findAll();
        assertThat(nominalDetTranss).hasSize(1);
        NominalDetTrans testNominalDetTrans = nominalDetTranss.iterator().next();
        assertThat(testNominalDetTrans.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteNominalDetTrans() throws Exception {
        // Initialize the database
        nominalDetTransRepository.saveAndFlush(nominalDetTrans);

        // Get the nominalDetTrans
        restNominalDetTransMockMvc.perform(delete("/api/nominalDetTranss/{id}", nominalDetTrans.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NominalDetTrans> nominalDetTranss = nominalDetTransRepository.findAll();
        assertThat(nominalDetTranss).hasSize(0);
    }
}
