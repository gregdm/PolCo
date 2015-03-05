package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.AdjectiveTrans;
import com.gregdm.polco.repository.AdjectiveTransRepository;

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
 * Test class for the AdjectiveTransResource REST controller.
 *
 * @see AdjectiveTransResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AdjectiveTransResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private AdjectiveTransRepository adjectiveTransRepository;

    private MockMvc restAdjectiveTransMockMvc;

    private AdjectiveTrans adjectiveTrans;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdjectiveTransResource adjectiveTransResource = new AdjectiveTransResource();
        ReflectionTestUtils.setField(adjectiveTransResource, "adjectiveTransRepository", adjectiveTransRepository);
        this.restAdjectiveTransMockMvc = MockMvcBuilders.standaloneSetup(adjectiveTransResource).build();
    }

    @Before
    public void initTest() {
        adjectiveTrans = new AdjectiveTrans();
        adjectiveTrans.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createAdjectiveTrans() throws Exception {
        // Validate the database is empty
        assertThat(adjectiveTransRepository.findAll()).hasSize(0);

        // Create the AdjectiveTrans
        restAdjectiveTransMockMvc.perform(post("/api/adjectiveTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adjectiveTrans)))
                .andExpect(status().isCreated());

        // Validate the AdjectiveTrans in the database
        List<AdjectiveTrans> adjectiveTranss = adjectiveTransRepository.findAll();
        assertThat(adjectiveTranss).hasSize(1);
        AdjectiveTrans testAdjectiveTrans = adjectiveTranss.iterator().next();
        assertThat(testAdjectiveTrans.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllAdjectiveTranss() throws Exception {
        // Initialize the database
        adjectiveTransRepository.saveAndFlush(adjectiveTrans);

        // Get all the adjectiveTranss
        restAdjectiveTransMockMvc.perform(get("/api/adjectiveTranss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(adjectiveTrans.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getAdjectiveTrans() throws Exception {
        // Initialize the database
        adjectiveTransRepository.saveAndFlush(adjectiveTrans);

        // Get the adjectiveTrans
        restAdjectiveTransMockMvc.perform(get("/api/adjectiveTranss/{id}", adjectiveTrans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adjectiveTrans.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdjectiveTrans() throws Exception {
        // Get the adjectiveTrans
        restAdjectiveTransMockMvc.perform(get("/api/adjectiveTranss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdjectiveTrans() throws Exception {
        // Initialize the database
        adjectiveTransRepository.saveAndFlush(adjectiveTrans);

        // Update the adjectiveTrans
        adjectiveTrans.setValue(UPDATED_VALUE);
        restAdjectiveTransMockMvc.perform(put("/api/adjectiveTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adjectiveTrans)))
                .andExpect(status().isOk());

        // Validate the AdjectiveTrans in the database
        List<AdjectiveTrans> adjectiveTranss = adjectiveTransRepository.findAll();
        assertThat(adjectiveTranss).hasSize(1);
        AdjectiveTrans testAdjectiveTrans = adjectiveTranss.iterator().next();
        assertThat(testAdjectiveTrans.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteAdjectiveTrans() throws Exception {
        // Initialize the database
        adjectiveTransRepository.saveAndFlush(adjectiveTrans);

        // Get the adjectiveTrans
        restAdjectiveTransMockMvc.perform(delete("/api/adjectiveTranss/{id}", adjectiveTrans.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AdjectiveTrans> adjectiveTranss = adjectiveTransRepository.findAll();
        assertThat(adjectiveTranss).hasSize(0);
    }
}
