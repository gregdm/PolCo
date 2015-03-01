package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.BadWord;
import com.gregdm.polco.repository.BadWordRepository;

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
 * Test class for the BadWordResource REST controller.
 *
 * @see BadWordResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BadWordResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";
    private static final String DEFAULT_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_TYPE = "UPDATED_TEXT";

    @Inject
    private BadWordRepository badWordRepository;

    private MockMvc restBadWordMockMvc;

    private BadWord badWord;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BadWordResource badWordResource = new BadWordResource();
        ReflectionTestUtils.setField(badWordResource, "badWordRepository", badWordRepository);
        this.restBadWordMockMvc = MockMvcBuilders.standaloneSetup(badWordResource).build();
    }

    @Before
    public void initTest() {
        badWord = new BadWord();
        badWord.setValue(DEFAULT_VALUE);
        badWord.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createBadWord() throws Exception {
        // Validate the database is empty
        assertThat(badWordRepository.findAll()).hasSize(0);

        // Create the BadWord
        restBadWordMockMvc.perform(post("/api/badWords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(badWord)))
                .andExpect(status().isCreated());

        // Validate the BadWord in the database
        List<BadWord> badWords = badWordRepository.findAll();
        assertThat(badWords).hasSize(1);
        BadWord testBadWord = badWords.iterator().next();
        assertThat(testBadWord.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testBadWord.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllBadWords() throws Exception {
        // Initialize the database
        badWordRepository.saveAndFlush(badWord);

        // Get all the badWords
        restBadWordMockMvc.perform(get("/api/badWords"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(badWord.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()))
                .andExpect(jsonPath("$.[0].type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getBadWord() throws Exception {
        // Initialize the database
        badWordRepository.saveAndFlush(badWord);

        // Get the badWord
        restBadWordMockMvc.perform(get("/api/badWords/{id}", badWord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(badWord.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBadWord() throws Exception {
        // Get the badWord
        restBadWordMockMvc.perform(get("/api/badWords/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBadWord() throws Exception {
        // Initialize the database
        badWordRepository.saveAndFlush(badWord);

        // Update the badWord
        badWord.setValue(UPDATED_VALUE);
        badWord.setType(UPDATED_TYPE);
        restBadWordMockMvc.perform(put("/api/badWords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(badWord)))
                .andExpect(status().isOk());

        // Validate the BadWord in the database
        List<BadWord> badWords = badWordRepository.findAll();
        assertThat(badWords).hasSize(1);
        BadWord testBadWord = badWords.iterator().next();
        assertThat(testBadWord.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testBadWord.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteBadWord() throws Exception {
        // Initialize the database
        badWordRepository.saveAndFlush(badWord);

        // Get the badWord
        restBadWordMockMvc.perform(delete("/api/badWords/{id}", badWord.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BadWord> badWords = badWordRepository.findAll();
        assertThat(badWords).hasSize(0);
    }
}
