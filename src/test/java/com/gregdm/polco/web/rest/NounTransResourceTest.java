package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.NounTrans;
import com.gregdm.polco.repository.NounTransRepository;

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
 * Test class for the NounTransResource REST controller.
 *
 * @see NounTransResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NounTransResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private NounTransRepository nounTransRepository;

    private MockMvc restNounTransMockMvc;

    private NounTrans nounTrans;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NounTransResource nounTransResource = new NounTransResource();
        ReflectionTestUtils.setField(nounTransResource, "nounTransRepository", nounTransRepository);
        this.restNounTransMockMvc = MockMvcBuilders.standaloneSetup(nounTransResource).build();
    }

    @Before
    public void initTest() {
        nounTrans = new NounTrans();
        nounTrans.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createNounTrans() throws Exception {
        // Validate the database is empty
        assertThat(nounTransRepository.findAll()).hasSize(0);

        // Create the NounTrans
        restNounTransMockMvc.perform(post("/api/nounTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nounTrans)))
                .andExpect(status().isCreated());

        // Validate the NounTrans in the database
        List<NounTrans> nounTranss = nounTransRepository.findAll();
        assertThat(nounTranss).hasSize(1);
        NounTrans testNounTrans = nounTranss.iterator().next();
        assertThat(testNounTrans.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllNounTranss() throws Exception {
        // Initialize the database
        nounTransRepository.saveAndFlush(nounTrans);

        // Get all the nounTranss
        restNounTransMockMvc.perform(get("/api/nounTranss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(nounTrans.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNounTrans() throws Exception {
        // Initialize the database
        nounTransRepository.saveAndFlush(nounTrans);

        // Get the nounTrans
        restNounTransMockMvc.perform(get("/api/nounTranss/{id}", nounTrans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(nounTrans.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNounTrans() throws Exception {
        // Get the nounTrans
        restNounTransMockMvc.perform(get("/api/nounTranss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNounTrans() throws Exception {
        // Initialize the database
        nounTransRepository.saveAndFlush(nounTrans);

        // Update the nounTrans
        nounTrans.setValue(UPDATED_VALUE);
        restNounTransMockMvc.perform(put("/api/nounTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nounTrans)))
                .andExpect(status().isOk());

        // Validate the NounTrans in the database
        List<NounTrans> nounTranss = nounTransRepository.findAll();
        assertThat(nounTranss).hasSize(1);
        NounTrans testNounTrans = nounTranss.iterator().next();
        assertThat(testNounTrans.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteNounTrans() throws Exception {
        // Initialize the database
        nounTransRepository.saveAndFlush(nounTrans);

        // Get the nounTrans
        restNounTransMockMvc.perform(delete("/api/nounTranss/{id}", nounTrans.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NounTrans> nounTranss = nounTransRepository.findAll();
        assertThat(nounTranss).hasSize(0);
    }
}
