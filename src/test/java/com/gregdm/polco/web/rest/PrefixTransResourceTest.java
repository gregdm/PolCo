package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.PrefixTrans;
import com.gregdm.polco.repository.PrefixTransRepository;

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
 * Test class for the PrefixTransResource REST controller.
 *
 * @see PrefixTransResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrefixTransResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private PrefixTransRepository prefixTransRepository;

    private MockMvc restPrefixTransMockMvc;

    private PrefixTrans prefixTrans;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrefixTransResource prefixTransResource = new PrefixTransResource();
        ReflectionTestUtils.setField(prefixTransResource, "prefixTransRepository", prefixTransRepository);
        this.restPrefixTransMockMvc = MockMvcBuilders.standaloneSetup(prefixTransResource).build();
    }

    @Before
    public void initTest() {
        prefixTrans = new PrefixTrans();
        prefixTrans.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createPrefixTrans() throws Exception {
        // Validate the database is empty
        assertThat(prefixTransRepository.findAll()).hasSize(0);

        // Create the PrefixTrans
        restPrefixTransMockMvc.perform(post("/api/prefixTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prefixTrans)))
                .andExpect(status().isCreated());

        // Validate the PrefixTrans in the database
        List<PrefixTrans> prefixTranss = prefixTransRepository.findAll();
        assertThat(prefixTranss).hasSize(1);
        PrefixTrans testPrefixTrans = prefixTranss.iterator().next();
        assertThat(testPrefixTrans.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllPrefixTranss() throws Exception {
        // Initialize the database
        prefixTransRepository.saveAndFlush(prefixTrans);

        // Get all the prefixTranss
        restPrefixTransMockMvc.perform(get("/api/prefixTranss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(prefixTrans.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getPrefixTrans() throws Exception {
        // Initialize the database
        prefixTransRepository.saveAndFlush(prefixTrans);

        // Get the prefixTrans
        restPrefixTransMockMvc.perform(get("/api/prefixTranss/{id}", prefixTrans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prefixTrans.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrefixTrans() throws Exception {
        // Get the prefixTrans
        restPrefixTransMockMvc.perform(get("/api/prefixTranss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrefixTrans() throws Exception {
        // Initialize the database
        prefixTransRepository.saveAndFlush(prefixTrans);

        // Update the prefixTrans
        prefixTrans.setValue(UPDATED_VALUE);
        restPrefixTransMockMvc.perform(put("/api/prefixTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prefixTrans)))
                .andExpect(status().isOk());

        // Validate the PrefixTrans in the database
        List<PrefixTrans> prefixTranss = prefixTransRepository.findAll();
        assertThat(prefixTranss).hasSize(1);
        PrefixTrans testPrefixTrans = prefixTranss.iterator().next();
        assertThat(testPrefixTrans.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deletePrefixTrans() throws Exception {
        // Initialize the database
        prefixTransRepository.saveAndFlush(prefixTrans);

        // Get the prefixTrans
        restPrefixTransMockMvc.perform(delete("/api/prefixTranss/{id}", prefixTrans.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PrefixTrans> prefixTranss = prefixTransRepository.findAll();
        assertThat(prefixTranss).hasSize(0);
    }
}
