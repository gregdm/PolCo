package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.InterjectionTrans;
import com.gregdm.polco.repository.InterjectionTransRepository;

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
 * Test class for the InterjectionTransResource REST controller.
 *
 * @see InterjectionTransResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InterjectionTransResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private InterjectionTransRepository interjectionTransRepository;

    private MockMvc restInterjectionTransMockMvc;

    private InterjectionTrans interjectionTrans;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InterjectionTransResource interjectionTransResource = new InterjectionTransResource();
        ReflectionTestUtils.setField(interjectionTransResource, "interjectionTransRepository", interjectionTransRepository);
        this.restInterjectionTransMockMvc = MockMvcBuilders.standaloneSetup(interjectionTransResource).build();
    }

    @Before
    public void initTest() {
        interjectionTrans = new InterjectionTrans();
        interjectionTrans.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createInterjectionTrans() throws Exception {
        // Validate the database is empty
        assertThat(interjectionTransRepository.findAll()).hasSize(0);

        // Create the InterjectionTrans
        restInterjectionTransMockMvc.perform(post("/api/interjectionTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(interjectionTrans)))
                .andExpect(status().isCreated());

        // Validate the InterjectionTrans in the database
        List<InterjectionTrans> interjectionTranss = interjectionTransRepository.findAll();
        assertThat(interjectionTranss).hasSize(1);
        InterjectionTrans testInterjectionTrans = interjectionTranss.iterator().next();
        assertThat(testInterjectionTrans.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllInterjectionTranss() throws Exception {
        // Initialize the database
        interjectionTransRepository.saveAndFlush(interjectionTrans);

        // Get all the interjectionTranss
        restInterjectionTransMockMvc.perform(get("/api/interjectionTranss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(interjectionTrans.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getInterjectionTrans() throws Exception {
        // Initialize the database
        interjectionTransRepository.saveAndFlush(interjectionTrans);

        // Get the interjectionTrans
        restInterjectionTransMockMvc.perform(get("/api/interjectionTranss/{id}", interjectionTrans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(interjectionTrans.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInterjectionTrans() throws Exception {
        // Get the interjectionTrans
        restInterjectionTransMockMvc.perform(get("/api/interjectionTranss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInterjectionTrans() throws Exception {
        // Initialize the database
        interjectionTransRepository.saveAndFlush(interjectionTrans);

        // Update the interjectionTrans
        interjectionTrans.setValue(UPDATED_VALUE);
        restInterjectionTransMockMvc.perform(put("/api/interjectionTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(interjectionTrans)))
                .andExpect(status().isOk());

        // Validate the InterjectionTrans in the database
        List<InterjectionTrans> interjectionTranss = interjectionTransRepository.findAll();
        assertThat(interjectionTranss).hasSize(1);
        InterjectionTrans testInterjectionTrans = interjectionTranss.iterator().next();
        assertThat(testInterjectionTrans.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteInterjectionTrans() throws Exception {
        // Initialize the database
        interjectionTransRepository.saveAndFlush(interjectionTrans);

        // Get the interjectionTrans
        restInterjectionTransMockMvc.perform(delete("/api/interjectionTranss/{id}", interjectionTrans.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InterjectionTrans> interjectionTranss = interjectionTransRepository.findAll();
        assertThat(interjectionTranss).hasSize(0);
    }
}
