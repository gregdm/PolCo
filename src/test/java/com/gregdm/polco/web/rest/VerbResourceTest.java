package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.Verb;
import com.gregdm.polco.repository.VerbRepository;

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
 * Test class for the VerbResource REST controller.
 *
 * @see VerbResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VerbResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";
    private static final String DEFAULT_TENSE = "SAMPLE_TEXT";
    private static final String UPDATED_TENSE = "UPDATED_TEXT";
    private static final String DEFAULT_PERSON = "SAMPLE_TEXT";
    private static final String UPDATED_PERSON = "UPDATED_TEXT";
    private static final String DEFAULT_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_NUMBER = "UPDATED_TEXT";

    @Inject
    private VerbRepository verbRepository;

    private MockMvc restVerbMockMvc;

    private Verb verb;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VerbResource verbResource = new VerbResource();
        ReflectionTestUtils.setField(verbResource, "verbRepository", verbRepository);
        this.restVerbMockMvc = MockMvcBuilders.standaloneSetup(verbResource).build();
    }

    @Before
    public void initTest() {
        verb = new Verb();
        verb.setValue(DEFAULT_VALUE);
        verb.setTense(DEFAULT_TENSE);
        verb.setPerson(DEFAULT_PERSON);
        verb.setNumber(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void createVerb() throws Exception {
        // Validate the database is empty
        assertThat(verbRepository.findAll()).hasSize(0);

        // Create the Verb
        restVerbMockMvc.perform(post("/api/verbs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verb)))
                .andExpect(status().isCreated());

        // Validate the Verb in the database
        List<Verb> verbs = verbRepository.findAll();
        assertThat(verbs).hasSize(1);
        Verb testVerb = verbs.iterator().next();
        assertThat(testVerb.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testVerb.getTense()).isEqualTo(DEFAULT_TENSE);
        assertThat(testVerb.getPerson()).isEqualTo(DEFAULT_PERSON);
        assertThat(testVerb.getNumber()).isEqualTo(DEFAULT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllVerbs() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        // Get all the verbs
        restVerbMockMvc.perform(get("/api/verbs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(verb.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()))
                .andExpect(jsonPath("$.[0].tense").value(DEFAULT_TENSE.toString()))
                .andExpect(jsonPath("$.[0].person").value(DEFAULT_PERSON.toString()))
                .andExpect(jsonPath("$.[0].number").value(DEFAULT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getVerb() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        // Get the verb
        restVerbMockMvc.perform(get("/api/verbs/{id}", verb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(verb.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.tense").value(DEFAULT_TENSE.toString()))
            .andExpect(jsonPath("$.person").value(DEFAULT_PERSON.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVerb() throws Exception {
        // Get the verb
        restVerbMockMvc.perform(get("/api/verbs/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVerb() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        // Update the verb
        verb.setValue(UPDATED_VALUE);
        verb.setTense(UPDATED_TENSE);
        verb.setPerson(UPDATED_PERSON);
        verb.setNumber(UPDATED_NUMBER);
        restVerbMockMvc.perform(put("/api/verbs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(verb)))
                .andExpect(status().isOk());

        // Validate the Verb in the database
        List<Verb> verbs = verbRepository.findAll();
        assertThat(verbs).hasSize(1);
        Verb testVerb = verbs.iterator().next();
        assertThat(testVerb.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testVerb.getTense()).isEqualTo(UPDATED_TENSE);
        assertThat(testVerb.getPerson()).isEqualTo(UPDATED_PERSON);
        assertThat(testVerb.getNumber()).isEqualTo(UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void deleteVerb() throws Exception {
        // Initialize the database
        verbRepository.saveAndFlush(verb);

        // Get the verb
        restVerbMockMvc.perform(delete("/api/verbs/{id}", verb.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Verb> verbs = verbRepository.findAll();
        assertThat(verbs).hasSize(0);
    }
}
