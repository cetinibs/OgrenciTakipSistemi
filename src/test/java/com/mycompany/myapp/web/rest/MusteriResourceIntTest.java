package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OgrenciTakipSistemiApp;

import com.mycompany.myapp.domain.Musteri;
import com.mycompany.myapp.repository.MusteriRepository;
import com.mycompany.myapp.repository.search.MusteriSearchRepository;
import com.mycompany.myapp.service.MusteriService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.ArrayList;
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
 * Test class for the MusteriResource REST controller.
 *
 * @see MusteriResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OgrenciTakipSistemiApp.class)
public class MusteriResourceIntTest {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    private static final String DEFAULT_SOYAD = "AAAAAAAAAA";
    private static final String UPDATED_SOYAD = "BBBBBBBBBB";

    @Autowired
    private MusteriRepository musteriRepository;

    @Mock
    private MusteriRepository musteriRepositoryMock;

    @Mock
    private MusteriService musteriServiceMock;

    @Autowired
    private MusteriService musteriService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.MusteriSearchRepositoryMockConfiguration
     */
    @Autowired
    private MusteriSearchRepository mockMusteriSearchRepository;

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

    private MockMvc restMusteriMockMvc;

    private Musteri musteri;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MusteriResource musteriResource = new MusteriResource(musteriService);
        this.restMusteriMockMvc = MockMvcBuilders.standaloneSetup(musteriResource)
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
    public static Musteri createEntity(EntityManager em) {
        Musteri musteri = new Musteri()
            .ad(DEFAULT_AD)
            .soyad(DEFAULT_SOYAD);
        return musteri;
    }

    @Before
    public void initTest() {
        musteri = createEntity(em);
    }

    @Test
    @Transactional
    public void createMusteri() throws Exception {
        int databaseSizeBeforeCreate = musteriRepository.findAll().size();

        // Create the Musteri
        restMusteriMockMvc.perform(post("/api/musteris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(musteri)))
            .andExpect(status().isCreated());

        // Validate the Musteri in the database
        List<Musteri> musteriList = musteriRepository.findAll();
        assertThat(musteriList).hasSize(databaseSizeBeforeCreate + 1);
        Musteri testMusteri = musteriList.get(musteriList.size() - 1);
        assertThat(testMusteri.getAd()).isEqualTo(DEFAULT_AD);
        assertThat(testMusteri.getSoyad()).isEqualTo(DEFAULT_SOYAD);

        // Validate the Musteri in Elasticsearch
        verify(mockMusteriSearchRepository, times(1)).save(testMusteri);
    }

    @Test
    @Transactional
    public void createMusteriWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = musteriRepository.findAll().size();

        // Create the Musteri with an existing ID
        musteri.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusteriMockMvc.perform(post("/api/musteris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(musteri)))
            .andExpect(status().isBadRequest());

        // Validate the Musteri in the database
        List<Musteri> musteriList = musteriRepository.findAll();
        assertThat(musteriList).hasSize(databaseSizeBeforeCreate);

        // Validate the Musteri in Elasticsearch
        verify(mockMusteriSearchRepository, times(0)).save(musteri);
    }

    @Test
    @Transactional
    public void getAllMusteris() throws Exception {
        // Initialize the database
        musteriRepository.saveAndFlush(musteri);

        // Get all the musteriList
        restMusteriMockMvc.perform(get("/api/musteris?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musteri.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD.toString())))
            .andExpect(jsonPath("$.[*].soyad").value(hasItem(DEFAULT_SOYAD.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllMusterisWithEagerRelationshipsIsEnabled() throws Exception {
        MusteriResource musteriResource = new MusteriResource(musteriServiceMock);
        when(musteriServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restMusteriMockMvc = MockMvcBuilders.standaloneSetup(musteriResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMusteriMockMvc.perform(get("/api/musteris?eagerload=true"))
        .andExpect(status().isOk());

        verify(musteriServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllMusterisWithEagerRelationshipsIsNotEnabled() throws Exception {
        MusteriResource musteriResource = new MusteriResource(musteriServiceMock);
            when(musteriServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restMusteriMockMvc = MockMvcBuilders.standaloneSetup(musteriResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restMusteriMockMvc.perform(get("/api/musteris?eagerload=true"))
        .andExpect(status().isOk());

            verify(musteriServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMusteri() throws Exception {
        // Initialize the database
        musteriRepository.saveAndFlush(musteri);

        // Get the musteri
        restMusteriMockMvc.perform(get("/api/musteris/{id}", musteri.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(musteri.getId().intValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD.toString()))
            .andExpect(jsonPath("$.soyad").value(DEFAULT_SOYAD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMusteri() throws Exception {
        // Get the musteri
        restMusteriMockMvc.perform(get("/api/musteris/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMusteri() throws Exception {
        // Initialize the database
        musteriService.save(musteri);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockMusteriSearchRepository);

        int databaseSizeBeforeUpdate = musteriRepository.findAll().size();

        // Update the musteri
        Musteri updatedMusteri = musteriRepository.findById(musteri.getId()).get();
        // Disconnect from session so that the updates on updatedMusteri are not directly saved in db
        em.detach(updatedMusteri);
        updatedMusteri
            .ad(UPDATED_AD)
            .soyad(UPDATED_SOYAD);

        restMusteriMockMvc.perform(put("/api/musteris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMusteri)))
            .andExpect(status().isOk());

        // Validate the Musteri in the database
        List<Musteri> musteriList = musteriRepository.findAll();
        assertThat(musteriList).hasSize(databaseSizeBeforeUpdate);
        Musteri testMusteri = musteriList.get(musteriList.size() - 1);
        assertThat(testMusteri.getAd()).isEqualTo(UPDATED_AD);
        assertThat(testMusteri.getSoyad()).isEqualTo(UPDATED_SOYAD);

        // Validate the Musteri in Elasticsearch
        verify(mockMusteriSearchRepository, times(1)).save(testMusteri);
    }

    @Test
    @Transactional
    public void updateNonExistingMusteri() throws Exception {
        int databaseSizeBeforeUpdate = musteriRepository.findAll().size();

        // Create the Musteri

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusteriMockMvc.perform(put("/api/musteris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(musteri)))
            .andExpect(status().isBadRequest());

        // Validate the Musteri in the database
        List<Musteri> musteriList = musteriRepository.findAll();
        assertThat(musteriList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Musteri in Elasticsearch
        verify(mockMusteriSearchRepository, times(0)).save(musteri);
    }

    @Test
    @Transactional
    public void deleteMusteri() throws Exception {
        // Initialize the database
        musteriService.save(musteri);

        int databaseSizeBeforeDelete = musteriRepository.findAll().size();

        // Delete the musteri
        restMusteriMockMvc.perform(delete("/api/musteris/{id}", musteri.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Musteri> musteriList = musteriRepository.findAll();
        assertThat(musteriList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Musteri in Elasticsearch
        verify(mockMusteriSearchRepository, times(1)).deleteById(musteri.getId());
    }

    @Test
    @Transactional
    public void searchMusteri() throws Exception {
        // Initialize the database
        musteriService.save(musteri);
        when(mockMusteriSearchRepository.search(queryStringQuery("id:" + musteri.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(musteri), PageRequest.of(0, 1), 1));
        // Search the musteri
        restMusteriMockMvc.perform(get("/api/_search/musteris?query=id:" + musteri.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(musteri.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)))
            .andExpect(jsonPath("$.[*].soyad").value(hasItem(DEFAULT_SOYAD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Musteri.class);
        Musteri musteri1 = new Musteri();
        musteri1.setId(1L);
        Musteri musteri2 = new Musteri();
        musteri2.setId(musteri1.getId());
        assertThat(musteri1).isEqualTo(musteri2);
        musteri2.setId(2L);
        assertThat(musteri1).isNotEqualTo(musteri2);
        musteri1.setId(null);
        assertThat(musteri1).isNotEqualTo(musteri2);
    }
}
