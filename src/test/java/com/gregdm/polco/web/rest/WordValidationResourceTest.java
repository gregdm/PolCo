package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.repository.WordValidationRepository;

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
 * Test class for the WordValidationResource REST controller.
 *
 * @see WordValidationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class WordValidationResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";
    private static final String DEFAULT_TRANSLATION = "SAMPLE_TEXT";
    private static final String UPDATED_TRANSLATION = "UPDATED_TEXT";
    private static final String DEFAULT_WORD_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_WORD_TYPE = "UPDATED_TEXT";
    private static final String DEFAULT_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_NUMBER = "UPDATED_TEXT";
    private static final String DEFAULT_GENDER = "SAMPLE_TEXT";
    private static final String UPDATED_GENDER = "UPDATED_TEXT";
    private static final String DEFAULT_PERSON = "SAMPLE_TEXT";
    private static final String UPDATED_PERSON = "UPDATED_TEXT";
    private static final String DEFAULT_TENSE = "SAMPLE_TEXT";
    private static final String UPDATED_TENSE = "UPDATED_TEXT";

    @Inject
    private WordValidationRepository wordValidationRepository;

    private MockMvc restWordValidationMockMvc;

    private WordValidation wordValidation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WordValidationResource wordValidationResource = new WordValidationResource();
        ReflectionTestUtils.setField(wordValidationResource, "wordValidationRepository", wordValidationRepository);
        this.restWordValidationMockMvc = MockMvcBuilders.standaloneSetup(wordValidationResource).build();
    }

    @Before
    public void initTest() {
        wordValidation = new WordValidation();
        wordValidation.setValue(DEFAULT_VALUE);
        wordValidation.setTranslation(DEFAULT_TRANSLATION);
        wordValidation.setWordType(DEFAULT_WORD_TYPE);
        wordValidation.setNumber(DEFAULT_NUMBER);
        wordValidation.setGender(DEFAULT_GENDER);
        wordValidation.setPerson(DEFAULT_PERSON);
        wordValidation.setTense(DEFAULT_TENSE);
    }

    @Test
    @Transactional
    public void createWordValidation() throws Exception {
        // Validate the database is empty
        assertThat(wordValidationRepository.findAll()).hasSize(0);

        // Create the WordValidation
        restWordValidationMockMvc.perform(post("/api/wordValidations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wordValidation)))
                .andExpect(status().isCreated());

        // Validate the WordValidation in the database
        List<WordValidation> wordValidations = wordValidationRepository.findAll();
        assertThat(wordValidations).hasSize(1);
        WordValidation testWordValidation = wordValidations.iterator().next();
        assertThat(testWordValidation.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testWordValidation.getTranslation()).isEqualTo(DEFAULT_TRANSLATION);
        assertThat(testWordValidation.getWordType()).isEqualTo(DEFAULT_WORD_TYPE);
        assertThat(testWordValidation.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testWordValidation.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testWordValidation.getPerson()).isEqualTo(DEFAULT_PERSON);
        assertThat(testWordValidation.getTense()).isEqualTo(DEFAULT_TENSE);
    }

    @Test
    @Transactional
    public void getAllWordValidations() throws Exception {
        // Initialize the database
        wordValidationRepository.saveAndFlush(wordValidation);

        // Get all the wordValidations
        restWordValidationMockMvc.perform(get("/api/wordValidations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(wordValidation.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()))
                .andExpect(jsonPath("$.[0].translation").value(DEFAULT_TRANSLATION.toString()))
                .andExpect(jsonPath("$.[0].wordType").value(DEFAULT_WORD_TYPE.toString()))
                .andExpect(jsonPath("$.[0].number").value(DEFAULT_NUMBER.toString()))
                .andExpect(jsonPath("$.[0].gender").value(DEFAULT_GENDER.toString()))
                .andExpect(jsonPath("$.[0].person").value(DEFAULT_PERSON.toString()))
                .andExpect(jsonPath("$.[0].tense").value(DEFAULT_TENSE.toString()));
    }

    @Test
    @Transactional
    public void getWordValidation() throws Exception {
        // Initialize the database
        wordValidationRepository.saveAndFlush(wordValidation);

        // Get the wordValidation
        restWordValidationMockMvc.perform(get("/api/wordValidations/{id}", wordValidation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(wordValidation.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.translation").value(DEFAULT_TRANSLATION.toString()))
            .andExpect(jsonPath("$.wordType").value(DEFAULT_WORD_TYPE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.person").value(DEFAULT_PERSON.toString()))
            .andExpect(jsonPath("$.tense").value(DEFAULT_TENSE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWordValidation() throws Exception {
        // Get the wordValidation
        restWordValidationMockMvc.perform(get("/api/wordValidations/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWordValidation() throws Exception {
        // Initialize the database
        wordValidationRepository.saveAndFlush(wordValidation);

        // Update the wordValidation
        wordValidation.setValue(UPDATED_VALUE);
        wordValidation.setTranslation(UPDATED_TRANSLATION);
        wordValidation.setWordType(UPDATED_WORD_TYPE);
        wordValidation.setNumber(UPDATED_NUMBER);
        wordValidation.setGender(UPDATED_GENDER);
        wordValidation.setPerson(UPDATED_PERSON);
        wordValidation.setTense(UPDATED_TENSE);
        restWordValidationMockMvc.perform(put("/api/wordValidations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wordValidation)))
                .andExpect(status().isOk());

        // Validate the WordValidation in the database
        List<WordValidation> wordValidations = wordValidationRepository.findAll();
        assertThat(wordValidations).hasSize(1);
        WordValidation testWordValidation = wordValidations.iterator().next();
        assertThat(testWordValidation.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testWordValidation.getTranslation()).isEqualTo(UPDATED_TRANSLATION);
        assertThat(testWordValidation.getWordType()).isEqualTo(UPDATED_WORD_TYPE);
        assertThat(testWordValidation.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testWordValidation.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testWordValidation.getPerson()).isEqualTo(UPDATED_PERSON);
        assertThat(testWordValidation.getTense()).isEqualTo(UPDATED_TENSE);
    }

    @Test
    @Transactional
    public void deleteWordValidation() throws Exception {
        // Initialize the database
        wordValidationRepository.saveAndFlush(wordValidation);

        // Get the wordValidation
        restWordValidationMockMvc.perform(delete("/api/wordValidations/{id}", wordValidation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WordValidation> wordValidations = wordValidationRepository.findAll();
        assertThat(wordValidations).hasSize(0);
    }
}
