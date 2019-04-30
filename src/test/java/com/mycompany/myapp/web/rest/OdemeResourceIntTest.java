package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OgrenciTakipSistemiApp;

import com.mycompany.myapp.domain.Odeme;
import com.mycompany.myapp.repository.OdemeRepository;
import com.mycompany.myapp.repository.search.OdemeSearchRepository;
import com.mycompany.myapp.service.OdemeService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OdemeResource REST controller.
 *
 * @see OdemeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OgrenciTakipSistemiApp.class)
public class OdemeResourceIntTest {

    private static final LocalDate DEFAULT_TARIH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TARIH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ODEME_ADI = "AAAAAAAAAA";
    private static final String UPDATED_ODEME_ADI = "BBBBBBBBBB";

    private static final String DEFAULT_ODEME_DETAYI = "AAAAAAAAAA";
    private static final String UPDATED_ODEME_DETAYI = "BBBBBBBBBB";

    private static final Long DEFAULT_ODEME = 1L;
    private static final Long UPDATED_ODEME = 2L;

    @Autowired
    private OdemeRepository odemeRepository;

    @Autowired
    private OdemeService odemeService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.OdemeSearchRepositoryMockConfiguration
     */
    @Autowired
    private OdemeSearchRepository mockOdemeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restOdemeMockMvc;

    private Odeme odeme;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OdemeResource odemeResource = new OdemeResource(odemeService);
        this.restOdemeMockMvc = MockMvcBuilders.standaloneSetup(odemeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Odeme createEntity(EntityManager em) {
        Odeme odeme = new Odeme()
            .tarih(DEFAULT_TARIH)
            .odemeAdi(DEFAULT_ODEME_ADI)
            .odemeDetayi(DEFAULT_ODEME_DETAYI)
            .odeme(DEFAULT_ODEME);
        return odeme;
    }

    @Before
    public void initTest() {
        odeme = createEntity(em);
    }

    @Test
    @Transactional
    public void createOdeme() throws Exception {
        int databaseSizeBeforeCreate = odemeRepository.findAll().size();

        // Create the Odeme
        restOdemeMockMvc.perform(post("/api/odemes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(odeme)))
            .andExpect(status().isCreated());

        // Validate the Odeme in the database
        List<Odeme> odemeList = odemeRepository.findAll();
        assertThat(odemeList).hasSize(databaseSizeBeforeCreate + 1);
        Odeme testOdeme = odemeList.get(odemeList.size() - 1);
        assertThat(testOdeme.getTarih()).isEqualTo(DEFAULT_TARIH);
        assertThat(testOdeme.getOdemeAdi()).isEqualTo(DEFAULT_ODEME_ADI);
        assertThat(testOdeme.getOdemeDetayi()).isEqualTo(DEFAULT_ODEME_DETAYI);
        assertThat(testOdeme.getOdeme()).isEqualTo(DEFAULT_ODEME);

        // Validate the Odeme in Elasticsearch
        verify(mockOdemeSearchRepository, times(1)).save(testOdeme);
    }

    @Test
    @Transactional
    public void createOdemeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = odemeRepository.findAll().size();

        // Create the Odeme with an existing ID
        odeme.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOdemeMockMvc.perform(post("/api/odemes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(odeme)))
            .andExpect(status().isBadRequest());

        // Validate the Odeme in the database
        List<Odeme> odemeList = odemeRepository.findAll();
        assertThat(odemeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Odeme in Elasticsearch
        verify(mockOdemeSearchRepository, times(0)).save(odeme);
    }

    @Test
    @Transactional
    public void getAllOdemes() throws Exception {
        // Initialize the database
        odemeRepository.saveAndFlush(odeme);

        // Get all the odemeList
        restOdemeMockMvc.perform(get("/api/odemes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(odeme.getId().intValue())))
            .andExpect(jsonPath("$.[*].tarih").value(hasItem(DEFAULT_TARIH.toString())))
            .andExpect(jsonPath("$.[*].odemeAdi").value(hasItem(DEFAULT_ODEME_ADI.toString())))
            .andExpect(jsonPath("$.[*].odemeDetayi").value(hasItem(DEFAULT_ODEME_DETAYI.toString())))
            .andExpect(jsonPath("$.[*].odeme").value(hasItem(DEFAULT_ODEME.intValue())));
    }
    
    @Test
    @Transactional
    public void getOdeme() throws Exception {
        // Initialize the database
        odemeRepository.saveAndFlush(odeme);

        // Get the odeme
        restOdemeMockMvc.perform(get("/api/odemes/{id}", odeme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(odeme.getId().intValue()))
            .andExpect(jsonPath("$.tarih").value(DEFAULT_TARIH.toString()))
            .andExpect(jsonPath("$.odemeAdi").value(DEFAULT_ODEME_ADI.toString()))
            .andExpect(jsonPath("$.odemeDetayi").value(DEFAULT_ODEME_DETAYI.toString()))
            .andExpect(jsonPath("$.odeme").value(DEFAULT_ODEME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOdeme() throws Exception {
        // Get the odeme
        restOdemeMockMvc.perform(get("/api/odemes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOdeme() throws Exception {
        // Initialize the database
        odemeService.save(odeme);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockOdemeSearchRepository);

        int databaseSizeBeforeUpdate = odemeRepository.findAll().size();

        // Update the odeme
        Odeme updatedOdeme = odemeRepository.findById(odeme.getId()).get();
        // Disconnect from session so that the updates on updatedOdeme are not directly saved in db
        em.detach(updatedOdeme);
        updatedOdeme
            .tarih(UPDATED_TARIH)
            .odemeAdi(UPDATED_ODEME_ADI)
            .odemeDetayi(UPDATED_ODEME_DETAYI)
            .odeme(UPDATED_ODEME);

        restOdemeMockMvc.perform(put("/api/odemes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOdeme)))
            .andExpect(status().isOk());

        // Validate the Odeme in the database
        List<Odeme> odemeList = odemeRepository.findAll();
        assertThat(odemeList).hasSize(databaseSizeBeforeUpdate);
        Odeme testOdeme = odemeList.get(odemeList.size() - 1);
        assertThat(testOdeme.getTarih()).isEqualTo(UPDATED_TARIH);
        assertThat(testOdeme.getOdemeAdi()).isEqualTo(UPDATED_ODEME_ADI);
        assertThat(testOdeme.getOdemeDetayi()).isEqualTo(UPDATED_ODEME_DETAYI);
        assertThat(testOdeme.getOdeme()).isEqualTo(UPDATED_ODEME);

        // Validate the Odeme in Elasticsearch
        verify(mockOdemeSearchRepository, times(1)).save(testOdeme);
    }

    @Test
    @Transactional
    public void updateNonExistingOdeme() throws Exception {
        int databaseSizeBeforeUpdate = odemeRepository.findAll().size();

        // Create the Odeme

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOdemeMockMvc.perform(put("/api/odemes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(odeme)))
            .andExpect(status().isBadRequest());

        // Validate the Odeme in the database
        List<Odeme> odemeList = odemeRepository.findAll();
        assertThat(odemeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Odeme in Elasticsearch
        verify(mockOdemeSearchRepository, times(0)).save(odeme);
    }

    @Test
    @Transactional
    public void deleteOdeme() throws Exception {
        // Initialize the database
        odemeService.save(odeme);

        int databaseSizeBeforeDelete = odemeRepository.findAll().size();

        // Delete the odeme
        restOdemeMockMvc.perform(delete("/api/odemes/{id}", odeme.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Odeme> odemeList = odemeRepository.findAll();
        assertThat(odemeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Odeme in Elasticsearch
        verify(mockOdemeSearchRepository, times(1)).deleteById(odeme.getId());
    }

    @Test
    @Transactional
    public void searchOdeme() throws Exception {
        // Initialize the database
        odemeService.save(odeme);
        when(mockOdemeSearchRepository.search(queryStringQuery("id:" + odeme.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(odeme), PageRequest.of(0, 1), 1));
        // Search the odeme
        restOdemeMockMvc.perform(get("/api/_search/odemes?query=id:" + odeme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(odeme.getId().intValue())))
            .andExpect(jsonPath("$.[*].tarih").value(hasItem(DEFAULT_TARIH.toString())))
            .andExpect(jsonPath("$.[*].odemeAdi").value(hasItem(DEFAULT_ODEME_ADI)))
            .andExpect(jsonPath("$.[*].odemeDetayi").value(hasItem(DEFAULT_ODEME_DETAYI)))
            .andExpect(jsonPath("$.[*].odeme").value(hasItem(DEFAULT_ODEME.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Odeme.class);
        Odeme odeme1 = new Odeme();
        odeme1.setId(1L);
        Odeme odeme2 = new Odeme();
        odeme2.setId(odeme1.getId());
        assertThat(odeme1).isEqualTo(odeme2);
        odeme2.setId(2L);
        assertThat(odeme1).isNotEqualTo(odeme2);
        odeme1.setId(null);
        assertThat(odeme1).isNotEqualTo(odeme2);
    }
}
