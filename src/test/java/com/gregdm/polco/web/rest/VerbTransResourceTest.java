package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.VerbTrans;
import com.gregdm.polco.repository.VerbTransRepository;

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
 * Test class for the VerbTransResource REST controller.
 *
 * @see VerbTransResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VerbTransResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private VerbTransRepository verbTransRepository;

    private MockMvc restVerbTransMockMvc;

    private VerbTrans verbTrans;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VerbTransResource verbTransResource = new VerbTransResource();
        ReflectionTestUtils.setField(verbTransResource, "verbTransRepository", verbTransRepository);
        this.restVerbTransMockMvc = MockMvcBuilders.standaloneSetup(verbTransResource).build();
    }

    @Before
    public void initTest() {
        verbTrans = new VerbTrans();
        verbTrans.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createVerbTrans() throws Exception {
        // Validate the database is empty
        assertThat(verbTransRepository.findAll()).hasSize(0);

        // Create the VerbTrans
        restVerbTransMockMvc.perform(post("/api/verbTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verbTrans)))
                .andExpect(status().isCreated());

        // Validate the VerbTrans in the database
        List<VerbTrans> verbTranss = verbTransRepository.findAll();
        assertThat(verbTranss).hasSize(1);
        VerbTrans testVerbTrans = verbTranss.iterator().next();
        assertThat(testVerbTrans.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllVerbTranss() throws Exception {
        // Initialize the database
        verbTransRepository.saveAndFlush(verbTrans);

        // Get all the verbTranss
        restVerbTransMockMvc.perform(get("/api/verbTranss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(verbTrans.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getVerbTrans() throws Exception {
        // Initialize the database
        verbTransRepository.saveAndFlush(verbTrans);

        // Get the verbTrans
        restVerbTransMockMvc.perform(get("/api/verbTranss/{id}", verbTrans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(verbTrans.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVerbTrans() throws Exception {
        // Get the verbTrans
        restVerbTransMockMvc.perform(get("/api/verbTranss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVerbTrans() throws Exception {
        // Initialize the database
        verbTransRepository.saveAndFlush(verbTrans);

        // Update the verbTrans
        verbTrans.setValue(UPDATED_VALUE);
        restVerbTransMockMvc.perform(put("/api/verbTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verbTrans)))
                .andExpect(status().isOk());

        // Validate the VerbTrans in the database
        List<VerbTrans> verbTranss = verbTransRepository.findAll();
        assertThat(verbTranss).hasSize(1);
        VerbTrans testVerbTrans = verbTranss.iterator().next();
        assertThat(testVerbTrans.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteVerbTrans() throws Exception {
        // Initialize the database
        verbTransRepository.saveAndFlush(verbTrans);

        // Get the verbTrans
        restVerbTransMockMvc.perform(delete("/api/verbTranss/{id}", verbTrans.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<VerbTrans> verbTranss = verbTransRepository.findAll();
        assertThat(verbTranss).hasSize(0);
    }
}
