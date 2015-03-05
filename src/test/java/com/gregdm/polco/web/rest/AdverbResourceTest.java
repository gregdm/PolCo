package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.Adverb;
import com.gregdm.polco.repository.AdverbRepository;

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
 * Test class for the AdverbResource REST controller.
 *
 * @see AdverbResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AdverbResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private AdverbRepository adverbRepository;

    private MockMvc restAdverbMockMvc;

    private Adverb adverb;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdverbResource adverbResource = new AdverbResource();
        ReflectionTestUtils.setField(adverbResource, "adverbRepository", adverbRepository);
        this.restAdverbMockMvc = MockMvcBuilders.standaloneSetup(adverbResource).build();
    }

    @Before
    public void initTest() {
        adverb = new Adverb();
        adverb.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createAdverb() throws Exception {
        // Validate the database is empty
        assertThat(adverbRepository.findAll()).hasSize(0);

        // Create the Adverb
        restAdverbMockMvc.perform(post("/api/adverbs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adverb)))
                .andExpect(status().isCreated());

        // Validate the Adverb in the database
        List<Adverb> adverbs = adverbRepository.findAll();
        assertThat(adverbs).hasSize(1);
        Adverb testAdverb = adverbs.iterator().next();
        assertThat(testAdverb.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllAdverbs() throws Exception {
        // Initialize the database
        adverbRepository.saveAndFlush(adverb);

        // Get all the adverbs
        restAdverbMockMvc.perform(get("/api/adverbs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(adverb.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getAdverb() throws Exception {
        // Initialize the database
        adverbRepository.saveAndFlush(adverb);

        // Get the adverb
        restAdverbMockMvc.perform(get("/api/adverbs/{id}", adverb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adverb.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdverb() throws Exception {
        // Get the adverb
        restAdverbMockMvc.perform(get("/api/adverbs/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdverb() throws Exception {
        // Initialize the database
        adverbRepository.saveAndFlush(adverb);

        // Update the adverb
        adverb.setValue(UPDATED_VALUE);
        restAdverbMockMvc.perform(put("/api/adverbs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adverb)))
                .andExpect(status().isOk());

        // Validate the Adverb in the database
        List<Adverb> adverbs = adverbRepository.findAll();
        assertThat(adverbs).hasSize(1);
        Adverb testAdverb = adverbs.iterator().next();
        assertThat(testAdverb.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteAdverb() throws Exception {
        // Initialize the database
        adverbRepository.saveAndFlush(adverb);

        // Get the adverb
        restAdverbMockMvc.perform(delete("/api/adverbs/{id}", adverb.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Adverb> adverbs = adverbRepository.findAll();
        assertThat(adverbs).hasSize(0);
    }
}
