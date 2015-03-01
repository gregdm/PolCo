package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.GoodWord;
import com.gregdm.polco.repository.GoodWordRepository;

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
 * Test class for the GoodWordResource REST controller.
 *
 * @see GoodWordResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GoodWordResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";
    private static final String DEFAULT_LEVEL = "SAMPLE_TEXT";
    private static final String UPDATED_LEVEL = "UPDATED_TEXT";

    @Inject
    private GoodWordRepository goodWordRepository;

    private MockMvc restGoodWordMockMvc;

    private GoodWord goodWord;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GoodWordResource goodWordResource = new GoodWordResource();
        ReflectionTestUtils.setField(goodWordResource, "goodWordRepository", goodWordRepository);
        this.restGoodWordMockMvc = MockMvcBuilders.standaloneSetup(goodWordResource).build();
    }

    @Before
    public void initTest() {
        goodWord = new GoodWord();
        goodWord.setValue(DEFAULT_VALUE);
        goodWord.setLevel(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createGoodWord() throws Exception {
        // Validate the database is empty
        assertThat(goodWordRepository.findAll()).hasSize(0);

        // Create the GoodWord
        restGoodWordMockMvc.perform(post("/api/goodWords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(goodWord)))
                .andExpect(status().isCreated());

        // Validate the GoodWord in the database
        List<GoodWord> goodWords = goodWordRepository.findAll();
        assertThat(goodWords).hasSize(1);
        GoodWord testGoodWord = goodWords.iterator().next();
        assertThat(testGoodWord.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testGoodWord.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void getAllGoodWords() throws Exception {
        // Initialize the database
        goodWordRepository.saveAndFlush(goodWord);

        // Get all the goodWords
        restGoodWordMockMvc.perform(get("/api/goodWords"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(goodWord.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()))
                .andExpect(jsonPath("$.[0].level").value(DEFAULT_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getGoodWord() throws Exception {
        // Initialize the database
        goodWordRepository.saveAndFlush(goodWord);

        // Get the goodWord
        restGoodWordMockMvc.perform(get("/api/goodWords/{id}", goodWord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(goodWord.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGoodWord() throws Exception {
        // Get the goodWord
        restGoodWordMockMvc.perform(get("/api/goodWords/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoodWord() throws Exception {
        // Initialize the database
        goodWordRepository.saveAndFlush(goodWord);

        // Update the goodWord
        goodWord.setValue(UPDATED_VALUE);
        goodWord.setLevel(UPDATED_LEVEL);
        restGoodWordMockMvc.perform(put("/api/goodWords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(goodWord)))
                .andExpect(status().isOk());

        // Validate the GoodWord in the database
        List<GoodWord> goodWords = goodWordRepository.findAll();
        assertThat(goodWords).hasSize(1);
        GoodWord testGoodWord = goodWords.iterator().next();
        assertThat(testGoodWord.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testGoodWord.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void deleteGoodWord() throws Exception {
        // Initialize the database
        goodWordRepository.saveAndFlush(goodWord);

        // Get the goodWord
        restGoodWordMockMvc.perform(delete("/api/goodWords/{id}", goodWord.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<GoodWord> goodWords = goodWordRepository.findAll();
        assertThat(goodWords).hasSize(0);
    }
}
