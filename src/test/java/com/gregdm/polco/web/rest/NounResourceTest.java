package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.Noun;
import com.gregdm.polco.repository.NounRepository;

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
 * Test class for the NounResource REST controller.
 *
 * @see NounResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NounResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";
    private static final String DEFAULT_GENDER = "SAMPLE_TEXT";
    private static final String UPDATED_GENDER = "UPDATED_TEXT";
    private static final String DEFAULT_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_NUMBER = "UPDATED_TEXT";
    private static final String DEFAULT_COMPOUND = "SAMPLE_TEXT";
    private static final String UPDATED_COMPOUND = "UPDATED_TEXT";

    @Inject
    private NounRepository nounRepository;

    private MockMvc restNounMockMvc;

    private Noun noun;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NounResource nounResource = new NounResource();
        ReflectionTestUtils.setField(nounResource, "nounRepository", nounRepository);
        this.restNounMockMvc = MockMvcBuilders.standaloneSetup(nounResource).build();
    }

    @Before
    public void initTest() {
        noun = new Noun();
        noun.setValue(DEFAULT_VALUE);
        noun.setGender(DEFAULT_GENDER);
        noun.setNumber(DEFAULT_NUMBER);
        noun.setCompound(DEFAULT_COMPOUND);
    }

    @Test
    @Transactional
    public void createNoun() throws Exception {
        // Validate the database is empty
        assertThat(nounRepository.findAll()).hasSize(0);

        // Create the Noun
        restNounMockMvc.perform(post("/api/nouns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noun)))
                .andExpect(status().isCreated());

        // Validate the Noun in the database
        List<Noun> nouns = nounRepository.findAll();
        assertThat(nouns).hasSize(1);
        Noun testNoun = nouns.iterator().next();
        assertThat(testNoun.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testNoun.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testNoun.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testNoun.getCompound()).isEqualTo(DEFAULT_COMPOUND);
    }

    @Test
    @Transactional
    public void getAllNouns() throws Exception {
        // Initialize the database
        nounRepository.saveAndFlush(noun);

        // Get all the nouns
        restNounMockMvc.perform(get("/api/nouns"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(noun.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()))
                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER.toString()))
                .andExpect(jsonPath("$.[0].number").value(DEFAULT_NUMBER.toString()))
                .andExpect(jsonPath("$.[0].compound").value(DEFAULT_COMPOUND.toString()));
    }

    @Test
    @Transactional
    public void getNoun() throws Exception {
        // Initialize the database
        nounRepository.saveAndFlush(noun);

        // Get the noun
        restNounMockMvc.perform(get("/api/nouns/{id}", noun.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(noun.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.compound").value(DEFAULT_COMPOUND.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNoun() throws Exception {
        // Get the noun
        restNounMockMvc.perform(get("/api/nouns/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNoun() throws Exception {
        // Initialize the database
        nounRepository.saveAndFlush(noun);

        // Update the noun
        noun.setValue(UPDATED_VALUE);
        noun.setGender(UPDATED_GENDER);
        noun.setNumber(UPDATED_NUMBER);
        noun.setCompound(UPDATED_COMPOUND);
        restNounMockMvc.perform(put("/api/nouns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noun)))
                .andExpect(status().isOk());

        // Validate the Noun in the database
        List<Noun> nouns = nounRepository.findAll();
        assertThat(nouns).hasSize(1);
        Noun testNoun = nouns.iterator().next();
        assertThat(testNoun.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testNoun.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testNoun.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testNoun.getCompound()).isEqualTo(UPDATED_COMPOUND);
    }

    @Test
    @Transactional
    public void deleteNoun() throws Exception {
        // Initialize the database
        nounRepository.saveAndFlush(noun);

        // Get the noun
        restNounMockMvc.perform(delete("/api/nouns/{id}", noun.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Noun> nouns = nounRepository.findAll();
        assertThat(nouns).hasSize(0);
    }
}
