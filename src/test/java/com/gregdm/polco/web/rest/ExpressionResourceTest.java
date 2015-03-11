package com.gregdm.polco.web.rest;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.Expression;
import com.gregdm.polco.repository.ExpressionRepository;

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
 * Test class for the ExpressionResource REST controller.
 *
 * @see ExpressionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExpressionResourceTest {

    private static final String DEFAULT_VALUE = "SAMPLE_TEXT";
    private static final String UPDATED_VALUE = "UPDATED_TEXT";

    @Inject
    private ExpressionRepository expressionRepository;

    private MockMvc restExpressionMockMvc;

    private Expression expression;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExpressionResource expressionResource = new ExpressionResource();
        ReflectionTestUtils.setField(expressionResource, "expressionRepository", expressionRepository);
        this.restExpressionMockMvc = MockMvcBuilders.standaloneSetup(expressionResource).build();
    }

    @Before
    public void initTest() {
        expression = new Expression();
        expression.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createExpression() throws Exception {
        // Validate the database is empty
        assertThat(expressionRepository.findAll()).hasSize(0);

        // Create the Expression
        restExpressionMockMvc.perform(post("/api/expressions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expression)))
                .andExpect(status().isCreated());

        // Validate the Expression in the database
        List<Expression> expressions = expressionRepository.findAll();
        assertThat(expressions).hasSize(1);
        Expression testExpression = expressions.iterator().next();
        assertThat(testExpression.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllExpressions() throws Exception {
        // Initialize the database
        expressionRepository.saveAndFlush(expression);

        // Get all the expressions
        restExpressionMockMvc.perform(get("/api/expressions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(expression.getId().intValue()))
                .andExpect(jsonPath("$.[0].value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getExpression() throws Exception {
        // Initialize the database
        expressionRepository.saveAndFlush(expression);

        // Get the expression
        restExpressionMockMvc.perform(get("/api/expressions/{id}", expression.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(expression.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExpression() throws Exception {
        // Get the expression
        restExpressionMockMvc.perform(get("/api/expressions/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpression() throws Exception {
        // Initialize the database
        expressionRepository.saveAndFlush(expression);

        // Update the expression
        expression.setValue(UPDATED_VALUE);
        restExpressionMockMvc.perform(put("/api/expressions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expression)))
                .andExpect(status().isOk());

        // Validate the Expression in the database
        List<Expression> expressions = expressionRepository.findAll();
        assertThat(expressions).hasSize(1);
        Expression testExpression = expressions.iterator().next();
        assertThat(testExpression.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteExpression() throws Exception {
        // Initialize the database
        expressionRepository.saveAndFlush(expression);

        // Get the expression
        restExpressionMockMvc.perform(delete("/api/expressions/{id}", expression.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Expression> expressions = expressionRepository.findAll();
        assertThat(expressions).hasSize(0);
    }
}
