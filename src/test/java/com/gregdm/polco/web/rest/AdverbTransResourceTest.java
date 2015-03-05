package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.AdverbTrans;
import com.gregdm.polco.repository.AdverbTransRepository;

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
 * Test class for the AdverbTransResource REST controller.
 *
 * @see AdverbTransResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AdverbTransResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private AdverbTransRepository adverbTransRepository;

    private MockMvc restAdverbTransMockMvc;

    private AdverbTrans adverbTrans;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdverbTransResource adverbTransResource = new AdverbTransResource();
        ReflectionTestUtils.setField(adverbTransResource, "adverbTransRepository", adverbTransRepository);
        this.restAdverbTransMockMvc = MockMvcBuilders.standaloneSetup(adverbTransResource).build();
    }

    @Before
    public void initTest() {
        adverbTrans = new AdverbTrans();
        adverbTrans.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createAdverbTrans() throws Exception {
        // Validate the database is empty
        assertThat(adverbTransRepository.findAll()).hasSize(0);

        // Create the AdverbTrans
        restAdverbTransMockMvc.perform(post("/api/adverbTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adverbTrans)))
                .andExpect(status().isCreated());

        // Validate the AdverbTrans in the database
        List<AdverbTrans> adverbTranss = adverbTransRepository.findAll();
        assertThat(adverbTranss).hasSize(1);
        AdverbTrans testAdverbTrans = adverbTranss.iterator().next();
        assertThat(testAdverbTrans.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllAdverbTranss() throws Exception {
        // Initialize the database
        adverbTransRepository.saveAndFlush(adverbTrans);

        // Get all the adverbTranss
        restAdverbTransMockMvc.perform(get("/api/adverbTranss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(adverbTrans.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getAdverbTrans() throws Exception {
        // Initialize the database
        adverbTransRepository.saveAndFlush(adverbTrans);

        // Get the adverbTrans
        restAdverbTransMockMvc.perform(get("/api/adverbTranss/{id}", adverbTrans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adverbTrans.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdverbTrans() throws Exception {
        // Get the adverbTrans
        restAdverbTransMockMvc.perform(get("/api/adverbTranss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdverbTrans() throws Exception {
        // Initialize the database
        adverbTransRepository.saveAndFlush(adverbTrans);

        // Update the adverbTrans
        adverbTrans.setValue(UPDATED_VALUE);
        restAdverbTransMockMvc.perform(put("/api/adverbTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adverbTrans)))
                .andExpect(status().isOk());

        // Validate the AdverbTrans in the database
        List<AdverbTrans> adverbTranss = adverbTransRepository.findAll();
        assertThat(adverbTranss).hasSize(1);
        AdverbTrans testAdverbTrans = adverbTranss.iterator().next();
        assertThat(testAdverbTrans.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteAdverbTrans() throws Exception {
        // Initialize the database
        adverbTransRepository.saveAndFlush(adverbTrans);

        // Get the adverbTrans
        restAdverbTransMockMvc.perform(delete("/api/adverbTranss/{id}", adverbTrans.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AdverbTrans> adverbTranss = adverbTransRepository.findAll();
        assertThat(adverbTranss).hasSize(0);
    }
}
