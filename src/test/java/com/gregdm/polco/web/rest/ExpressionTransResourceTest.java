package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.ExpressionTrans;
import com.gregdm.polco.repository.ExpressionTransRepository;

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
 * Test class for the ExpressionTransResource REST controller.
 *
 * @see ExpressionTransResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExpressionTransResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private ExpressionTransRepository expressionTransRepository;

    private MockMvc restExpressionTransMockMvc;

    private ExpressionTrans expressionTrans;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExpressionTransResource expressionTransResource = new ExpressionTransResource();
        ReflectionTestUtils.setField(expressionTransResource, "expressionTransRepository", expressionTransRepository);
        this.restExpressionTransMockMvc = MockMvcBuilders.standaloneSetup(expressionTransResource).build();
    }

    @Before
    public void initTest() {
        expressionTrans = new ExpressionTrans();
        expressionTrans.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createExpressionTrans() throws Exception {
        // Validate the database is empty
        assertThat(expressionTransRepository.findAll()).hasSize(0);

        // Create the ExpressionTrans
        restExpressionTransMockMvc.perform(post("/api/expressionTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expressionTrans)))
                .andExpect(status().isCreated());

        // Validate the ExpressionTrans in the database
        List<ExpressionTrans> expressionTranss = expressionTransRepository.findAll();
        assertThat(expressionTranss).hasSize(1);
        ExpressionTrans testExpressionTrans = expressionTranss.iterator().next();
        assertThat(testExpressionTrans.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllExpressionTranss() throws Exception {
        // Initialize the database
        expressionTransRepository.saveAndFlush(expressionTrans);

        // Get all the expressionTranss
        restExpressionTransMockMvc.perform(get("/api/expressionTranss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(expressionTrans.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getExpressionTrans() throws Exception {
        // Initialize the database
        expressionTransRepository.saveAndFlush(expressionTrans);

        // Get the expressionTrans
        restExpressionTransMockMvc.perform(get("/api/expressionTranss/{id}", expressionTrans.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(expressionTrans.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExpressionTrans() throws Exception {
        // Get the expressionTrans
        restExpressionTransMockMvc.perform(get("/api/expressionTranss/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpressionTrans() throws Exception {
        // Initialize the database
        expressionTransRepository.saveAndFlush(expressionTrans);

        // Update the expressionTrans
        expressionTrans.setValue(UPDATED_VALUE);
        restExpressionTransMockMvc.perform(put("/api/expressionTranss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expressionTrans)))
                .andExpect(status().isOk());

        // Validate the ExpressionTrans in the database
        List<ExpressionTrans> expressionTranss = expressionTransRepository.findAll();
        assertThat(expressionTranss).hasSize(1);
        ExpressionTrans testExpressionTrans = expressionTranss.iterator().next();
        assertThat(testExpressionTrans.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteExpressionTrans() throws Exception {
        // Initialize the database
        expressionTransRepository.saveAndFlush(expressionTrans);

        // Get the expressionTrans
        restExpressionTransMockMvc.perform(delete("/api/expressionTranss/{id}", expressionTrans.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ExpressionTrans> expressionTranss = expressionTransRepository.findAll();
        assertThat(expressionTranss).hasSize(0);
    }
}
