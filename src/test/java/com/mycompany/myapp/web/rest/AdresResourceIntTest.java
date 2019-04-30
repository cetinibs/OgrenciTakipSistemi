package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OgrenciTakipSistemiApp;

import com.mycompany.myapp.domain.Adres;
import com.mycompany.myapp.repository.AdresRepository;
import com.mycompany.myapp.repository.search.AdresSearchRepository;
import com.mycompany.myapp.service.AdresService;
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
 * Test class for the AdresResource REST controller.
 *
 * @see AdresResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OgrenciTakipSistemiApp.class)
public class AdresResourceIntTest {

    private static final String DEFAULT_EV_ADRES = "AAAAAAAAAA";
    private static final String UPDATED_EV_ADRES = "BBBBBBBBBB";

    private static final String DEFAULT_IS_ADRES = "AAAAAAAAAA";
    private static final String UPDATED_IS_ADRES = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON_CEP = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON_SABIT = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON_SABIT = "BBBBBBBBBB";

    @Autowired
    private AdresRepository adresRepository;

    @Autowired
    private AdresService adresService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.AdresSearchRepositoryMockConfiguration
     */
    @Autowired
    private AdresSearchRepository mockAdresSearchRepository;

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

    private MockMvc restAdresMockMvc;

    private Adres adres;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdresResource adresResource = new AdresResource(adresService);
        this.restAdresMockMvc = MockMvcBuilders.standaloneSetup(adresResource)
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
    public static Adres createEntity(EntityManager em) {
        Adres adres = new Adres()
            .evAdres(DEFAULT_EV_ADRES)
            .isAdres(DEFAULT_IS_ADRES)
            .email(DEFAULT_EMAIL)
            .telefonCep(DEFAULT_TELEFON_CEP)
            .telefonSabit(DEFAULT_TELEFON_SABIT);
        return adres;
    }

    @Before
    public void initTest() {
        adres = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdres() throws Exception {
        int databaseSizeBeforeCreate = adresRepository.findAll().size();

        // Create the Adres
        restAdresMockMvc.perform(post("/api/adres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adres)))
            .andExpect(status().isCreated());

        // Validate the Adres in the database
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeCreate + 1);
        Adres testAdres = adresList.get(adresList.size() - 1);
        assertThat(testAdres.getEvAdres()).isEqualTo(DEFAULT_EV_ADRES);
        assertThat(testAdres.getIsAdres()).isEqualTo(DEFAULT_IS_ADRES);
        assertThat(testAdres.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAdres.getTelefonCep()).isEqualTo(DEFAULT_TELEFON_CEP);
        assertThat(testAdres.getTelefonSabit()).isEqualTo(DEFAULT_TELEFON_SABIT);

        // Validate the Adres in Elasticsearch
        verify(mockAdresSearchRepository, times(1)).save(testAdres);
    }

    @Test
    @Transactional
    public void createAdresWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adresRepository.findAll().size();

        // Create the Adres with an existing ID
        adres.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdresMockMvc.perform(post("/api/adres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adres)))
            .andExpect(status().isBadRequest());

        // Validate the Adres in the database
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeCreate);

        // Validate the Adres in Elasticsearch
        verify(mockAdresSearchRepository, times(0)).save(adres);
    }

    @Test
    @Transactional
    public void getAllAdres() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get all the adresList
        restAdresMockMvc.perform(get("/api/adres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adres.getId().intValue())))
            .andExpect(jsonPath("$.[*].evAdres").value(hasItem(DEFAULT_EV_ADRES.toString())))
            .andExpect(jsonPath("$.[*].isAdres").value(hasItem(DEFAULT_IS_ADRES.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telefonCep").value(hasItem(DEFAULT_TELEFON_CEP.toString())))
            .andExpect(jsonPath("$.[*].telefonSabit").value(hasItem(DEFAULT_TELEFON_SABIT.toString())));
    }
    
    @Test
    @Transactional
    public void getAdres() throws Exception {
        // Initialize the database
        adresRepository.saveAndFlush(adres);

        // Get the adres
        restAdresMockMvc.perform(get("/api/adres/{id}", adres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adres.getId().intValue()))
            .andExpect(jsonPath("$.evAdres").value(DEFAULT_EV_ADRES.toString()))
            .andExpect(jsonPath("$.isAdres").value(DEFAULT_IS_ADRES.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.telefonCep").value(DEFAULT_TELEFON_CEP.toString()))
            .andExpect(jsonPath("$.telefonSabit").value(DEFAULT_TELEFON_SABIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdres() throws Exception {
        // Get the adres
        restAdresMockMvc.perform(get("/api/adres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdres() throws Exception {
        // Initialize the database
        adresService.save(adres);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAdresSearchRepository);

        int databaseSizeBeforeUpdate = adresRepository.findAll().size();

        // Update the adres
        Adres updatedAdres = adresRepository.findById(adres.getId()).get();
        // Disconnect from session so that the updates on updatedAdres are not directly saved in db
        em.detach(updatedAdres);
        updatedAdres
            .evAdres(UPDATED_EV_ADRES)
            .isAdres(UPDATED_IS_ADRES)
            .email(UPDATED_EMAIL)
            .telefonCep(UPDATED_TELEFON_CEP)
            .telefonSabit(UPDATED_TELEFON_SABIT);

        restAdresMockMvc.perform(put("/api/adres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdres)))
            .andExpect(status().isOk());

        // Validate the Adres in the database
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeUpdate);
        Adres testAdres = adresList.get(adresList.size() - 1);
        assertThat(testAdres.getEvAdres()).isEqualTo(UPDATED_EV_ADRES);
        assertThat(testAdres.getIsAdres()).isEqualTo(UPDATED_IS_ADRES);
        assertThat(testAdres.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAdres.getTelefonCep()).isEqualTo(UPDATED_TELEFON_CEP);
        assertThat(testAdres.getTelefonSabit()).isEqualTo(UPDATED_TELEFON_SABIT);

        // Validate the Adres in Elasticsearch
        verify(mockAdresSearchRepository, times(1)).save(testAdres);
    }

    @Test
    @Transactional
    public void updateNonExistingAdres() throws Exception {
        int databaseSizeBeforeUpdate = adresRepository.findAll().size();

        // Create the Adres

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdresMockMvc.perform(put("/api/adres")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adres)))
            .andExpect(status().isBadRequest());

        // Validate the Adres in the database
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Adres in Elasticsearch
        verify(mockAdresSearchRepository, times(0)).save(adres);
    }

    @Test
    @Transactional
    public void deleteAdres() throws Exception {
        // Initialize the database
        adresService.save(adres);

        int databaseSizeBeforeDelete = adresRepository.findAll().size();

        // Delete the adres
        restAdresMockMvc.perform(delete("/api/adres/{id}", adres.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Adres> adresList = adresRepository.findAll();
        assertThat(adresList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Adres in Elasticsearch
        verify(mockAdresSearchRepository, times(1)).deleteById(adres.getId());
    }

    @Test
    @Transactional
    public void searchAdres() throws Exception {
        // Initialize the database
        adresService.save(adres);
        when(mockAdresSearchRepository.search(queryStringQuery("id:" + adres.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(adres), PageRequest.of(0, 1), 1));
        // Search the adres
        restAdresMockMvc.perform(get("/api/_search/adres?query=id:" + adres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adres.getId().intValue())))
            .andExpect(jsonPath("$.[*].evAdres").value(hasItem(DEFAULT_EV_ADRES)))
            .andExpect(jsonPath("$.[*].isAdres").value(hasItem(DEFAULT_IS_ADRES)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefonCep").value(hasItem(DEFAULT_TELEFON_CEP)))
            .andExpect(jsonPath("$.[*].telefonSabit").value(hasItem(DEFAULT_TELEFON_SABIT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adres.class);
        Adres adres1 = new Adres();
        adres1.setId(1L);
        Adres adres2 = new Adres();
        adres2.setId(adres1.getId());
        assertThat(adres1).isEqualTo(adres2);
        adres2.setId(2L);
        assertThat(adres1).isNotEqualTo(adres2);
        adres1.setId(null);
        assertThat(adres1).isNotEqualTo(adres2);
    }
}
