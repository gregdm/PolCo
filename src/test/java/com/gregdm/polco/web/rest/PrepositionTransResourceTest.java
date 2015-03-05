package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.PrepositionTrans;
import com.gregdm.polco.repository.PrepositionTransRepository;

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
 * Test class for the PrepositionTransResource REST controller.
 *
 * @see PrepositionTransResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrepositionTransResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private PrepositionTransRepository prepositionTransRepository;

    private MockMvc restPrepositionTransMockMvc;

    private PrepositionTrans prepositionTrans;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrepositionTransResource prepositionTransResource = new PrepositionTransResource();
        ReflectionTestUtils.setField(prepositionTransResource, "prepositionTransRepository", prepositionTransRepository);
        this.restPrepositionTransMockMvc = MockMvcBuilders.standaloneSetup(prepositionTransResource).build();
    }

    @Before
    public void initTest() {
        prepositionTrans = new PrepositionTrans();
        prepositionTrans.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createPrepositionTrans() throws Exception {
        // Validate the database is empty
        assertThat(prepositionTransRepository.findAll()).hasSize(0);

        // Create the PrepositionTrans
        restPrepositionTransMockMvc.perform(post("/api/prepositionTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prepositionTrans)))
                .andExpect(status().isCreated());

        // Validate the PrepositionTrans in the database
        List<PrepositionTrans> prepositionTranss = prepositionTransRepository.findAll();
        assertThat(prepositionTranss).hasSize(1);
        PrepositionTrans testPrepositionTrans = prepositionTranss.iterator().next();
        assertThat(testPrepositionTrans.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllPrepositionTranss() throws Exception {
        // Initialize the database
        prepositionTransRepository.saveAndFlush(prepositionTrans);

        // Get all the prepositionTranss
        restPrepositionTransMockMvc.perform(get("/api/prepositionTranss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(prepositionTrans.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getPrepositionTrans() throws Exception {
        // Initialize the database
        prepositionTransRepository.saveAndFlush(prepositionTrans);

        // Get the prepositionTrans
        restPrepositionTransMockMvc.perform(get("/api/prepositionTranss/{id}", prepositionTrans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prepositionTrans.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrepositionTrans() throws Exception {
        // Get the prepositionTrans
        restPrepositionTransMockMvc.perform(get("/api/prepositionTranss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrepositionTrans() throws Exception {
        // Initialize the database
        prepositionTransRepository.saveAndFlush(prepositionTrans);

        // Update the prepositionTrans
        prepositionTrans.setValue(UPDATED_VALUE);
        restPrepositionTransMockMvc.perform(put("/api/prepositionTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prepositionTrans)))
                .andExpect(status().isOk());

        // Validate the PrepositionTrans in the database
        List<PrepositionTrans> prepositionTranss = prepositionTransRepository.findAll();
        assertThat(prepositionTranss).hasSize(1);
        PrepositionTrans testPrepositionTrans = prepositionTranss.iterator().next();
        assertThat(testPrepositionTrans.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deletePrepositionTrans() throws Exception {
        // Initialize the database
        prepositionTransRepository.saveAndFlush(prepositionTrans);

        // Get the prepositionTrans
        restPrepositionTransMockMvc.perform(delete("/api/prepositionTranss/{id}", prepositionTrans.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PrepositionTrans> prepositionTranss = prepositionTransRepository.findAll();
        assertThat(prepositionTranss).hasSize(0);
    }
}
