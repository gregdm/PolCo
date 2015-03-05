package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.Prefix;
import com.gregdm.polco.repository.PrefixRepository;

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
 * Test class for the PrefixResource REST controller.
 *
 * @see PrefixResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrefixResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private PrefixRepository prefixRepository;

    private MockMvc restPrefixMockMvc;

    private Prefix prefix;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrefixResource prefixResource = new PrefixResource();
        ReflectionTestUtils.setField(prefixResource, "prefixRepository", prefixRepository);
        this.restPrefixMockMvc = MockMvcBuilders.standaloneSetup(prefixResource).build();
    }

    @Before
    public void initTest() {
        prefix = new Prefix();
        prefix.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createPrefix() throws Exception {
        // Validate the database is empty
        assertThat(prefixRepository.findAll()).hasSize(0);

        // Create the Prefix
        restPrefixMockMvc.perform(post("/api/prefixs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prefix)))
                .andExpect(status().isCreated());

        // Validate the Prefix in the database
        List<Prefix> prefixs = prefixRepository.findAll();
        assertThat(prefixs).hasSize(1);
        Prefix testPrefix = prefixs.iterator().next();
        assertThat(testPrefix.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllPrefixs() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

        // Get all the prefixs
        restPrefixMockMvc.perform(get("/api/prefixs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(prefix.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getPrefix() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

        // Get the prefix
        restPrefixMockMvc.perform(get("/api/prefixs/{id}", prefix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(prefix.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrefix() throws Exception {
        // Get the prefix
        restPrefixMockMvc.perform(get("/api/prefixs/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrefix() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

        // Update the prefix
        prefix.setValue(UPDATED_VALUE);
        restPrefixMockMvc.perform(put("/api/prefixs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(prefix)))
                .andExpect(status().isOk());

        // Validate the Prefix in the database
        List<Prefix> prefixs = prefixRepository.findAll();
        assertThat(prefixs).hasSize(1);
        Prefix testPrefix = prefixs.iterator().next();
        assertThat(testPrefix.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deletePrefix() throws Exception {
        // Initialize the database
        prefixRepository.saveAndFlush(prefix);

        // Get the prefix
        restPrefixMockMvc.perform(delete("/api/prefixs/{id}", prefix.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Prefix> prefixs = prefixRepository.findAll();
        assertThat(prefixs).hasSize(0);
    }
}
