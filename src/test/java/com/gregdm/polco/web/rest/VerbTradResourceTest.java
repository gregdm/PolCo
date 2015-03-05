package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.VerbTrad;
import com.gregdm.polco.repository.VerbTradRepository;

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
 * Test class for the VerbTradResource REST controller.
 *
 * @see VerbTradResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VerbTradResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private VerbTradRepository verbTradRepository;

    private MockMvc restVerbTradMockMvc;

    private VerbTrad verbTrad;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VerbTradResource verbTradResource = new VerbTradResource();
        ReflectionTestUtils.setField(verbTradResource, "verbTradRepository", verbTradRepository);
        this.restVerbTradMockMvc = MockMvcBuilders.standaloneSetup(verbTradResource).build();
    }

    @Before
    public void initTest() {
        verbTrad = new VerbTrad();
        verbTrad.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createVerbTrad() throws Exception {
        // Validate the database is empty
        assertThat(verbTradRepository.findAll()).hasSize(0);

        // Create the VerbTrad
        restVerbTradMockMvc.perform(post("/api/verbTrads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verbTrad)))
                .andExpect(status().isCreated());

        // Validate the VerbTrad in the database
        List<VerbTrad> verbTrads = verbTradRepository.findAll();
        assertThat(verbTrads).hasSize(1);
        VerbTrad testVerbTrad = verbTrads.iterator().next();
        assertThat(testVerbTrad.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllVerbTrads() throws Exception {
        // Initialize the database
        verbTradRepository.saveAndFlush(verbTrad);

        // Get all the verbTrads
        restVerbTradMockMvc.perform(get("/api/verbTrads"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(verbTrad.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getVerbTrad() throws Exception {
        // Initialize the database
        verbTradRepository.saveAndFlush(verbTrad);

        // Get the verbTrad
        restVerbTradMockMvc.perform(get("/api/verbTrads/{id}", verbTrad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(verbTrad.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVerbTrad() throws Exception {
        // Get the verbTrad
        restVerbTradMockMvc.perform(get("/api/verbTrads/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVerbTrad() throws Exception {
        // Initialize the database
        verbTradRepository.saveAndFlush(verbTrad);

        // Update the verbTrad
        verbTrad.setValue(UPDATED_VALUE);
        restVerbTradMockMvc.perform(put("/api/verbTrads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verbTrad)))
                .andExpect(status().isOk());

        // Validate the VerbTrad in the database
        List<VerbTrad> verbTrads = verbTradRepository.findAll();
        assertThat(verbTrads).hasSize(1);
        VerbTrad testVerbTrad = verbTrads.iterator().next();
        assertThat(testVerbTrad.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteVerbTrad() throws Exception {
        // Initialize the database
        verbTradRepository.saveAndFlush(verbTrad);

        // Get the verbTrad
        restVerbTradMockMvc.perform(delete("/api/verbTrads/{id}", verbTrad.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<VerbTrad> verbTrads = verbTradRepository.findAll();
        assertThat(verbTrads).hasSize(0);
    }
}
