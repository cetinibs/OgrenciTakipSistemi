package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OgrenciTakipSistemiApp;

import com.mycompany.myapp.domain.Veli;
import com.mycompany.myapp.repository.VeliRepository;
import com.mycompany.myapp.repository.search.VeliSearchRepository;
import com.mycompany.myapp.service.VeliService;
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
 * Test class for the VeliResource REST controller.
 *
 * @see VeliResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OgrenciTakipSistemiApp.class)
public class VeliResourceIntTest {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    private static final String DEFAULT_SOYAD = "AAAAAAAAAA";
    private static final String UPDATED_SOYAD = "BBBBBBBBBB";

    private static final String DEFAULT_ADRES = "AAAAAAAAAA";
    private static final String UPDATED_ADRES = "BBBBBBBBBB";

    private static final String DEFAULT_MESLEGI = "AAAAAAAAAA";
    private static final String UPDATED_MESLEGI = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON_CEP = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON_SABIT = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON_SABIT = "BBBBBBBBBB";

    @Autowired
    private VeliRepository veliRepository;

    @Autowired
    private VeliService veliService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.VeliSearchRepositoryMockConfiguration
     */
    @Autowired
    private VeliSearchRepository mockVeliSearchRepository;

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

    private MockMvc restVeliMockMvc;

    private Veli veli;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VeliResource veliResource = new VeliResource(veliService);
        this.restVeliMockMvc = MockMvcBuilders.standaloneSetup(veliResource)
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
    public static Veli createEntity(EntityManager em) {
        Veli veli = new Veli()
            .ad(DEFAULT_AD)
            .soyad(DEFAULT_SOYAD)
            .adres(DEFAULT_ADRES)
            .meslegi(DEFAULT_MESLEGI)
            .email(DEFAULT_EMAIL)
            .telefonCep(DEFAULT_TELEFON_CEP)
            .telefonSabit(DEFAULT_TELEFON_SABIT);
        return veli;
    }

    @Before
    public void initTest() {
        veli = createEntity(em);
    }

    @Test
    @Transactional
    public void createVeli() throws Exception {
        int databaseSizeBeforeCreate = veliRepository.findAll().size();

        // Create the Veli
        restVeliMockMvc.perform(post("/api/velis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veli)))
            .andExpect(status().isCreated());

        // Validate the Veli in the database
        List<Veli> veliList = veliRepository.findAll();
        assertThat(veliList).hasSize(databaseSizeBeforeCreate + 1);
        Veli testVeli = veliList.get(veliList.size() - 1);
        assertThat(testVeli.getAd()).isEqualTo(DEFAULT_AD);
        assertThat(testVeli.getSoyad()).isEqualTo(DEFAULT_SOYAD);
        assertThat(testVeli.getAdres()).isEqualTo(DEFAULT_ADRES);
        assertThat(testVeli.getMeslegi()).isEqualTo(DEFAULT_MESLEGI);
        assertThat(testVeli.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testVeli.getTelefonCep()).isEqualTo(DEFAULT_TELEFON_CEP);
        assertThat(testVeli.getTelefonSabit()).isEqualTo(DEFAULT_TELEFON_SABIT);

        // Validate the Veli in Elasticsearch
        verify(mockVeliSearchRepository, times(1)).save(testVeli);
    }

    @Test
    @Transactional
    public void createVeliWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = veliRepository.findAll().size();

        // Create the Veli with an existing ID
        veli.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVeliMockMvc.perform(post("/api/velis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veli)))
            .andExpect(status().isBadRequest());

        // Validate the Veli in the database
        List<Veli> veliList = veliRepository.findAll();
        assertThat(veliList).hasSize(databaseSizeBeforeCreate);

        // Validate the Veli in Elasticsearch
        verify(mockVeliSearchRepository, times(0)).save(veli);
    }

    @Test
    @Transactional
    public void getAllVelis() throws Exception {
        // Initialize the database
        veliRepository.saveAndFlush(veli);

        // Get all the veliList
        restVeliMockMvc.perform(get("/api/velis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veli.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD.toString())))
            .andExpect(jsonPath("$.[*].soyad").value(hasItem(DEFAULT_SOYAD.toString())))
            .andExpect(jsonPath("$.[*].adres").value(hasItem(DEFAULT_ADRES.toString())))
            .andExpect(jsonPath("$.[*].meslegi").value(hasItem(DEFAULT_MESLEGI.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telefonCep").value(hasItem(DEFAULT_TELEFON_CEP.toString())))
            .andExpect(jsonPath("$.[*].telefonSabit").value(hasItem(DEFAULT_TELEFON_SABIT.toString())));
    }
    
    @Test
    @Transactional
    public void getVeli() throws Exception {
        // Initialize the database
        veliRepository.saveAndFlush(veli);

        // Get the veli
        restVeliMockMvc.perform(get("/api/velis/{id}", veli.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(veli.getId().intValue()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD.toString()))
            .andExpect(jsonPath("$.soyad").value(DEFAULT_SOYAD.toString()))
            .andExpect(jsonPath("$.adres").value(DEFAULT_ADRES.toString()))
            .andExpect(jsonPath("$.meslegi").value(DEFAULT_MESLEGI.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.telefonCep").value(DEFAULT_TELEFON_CEP.toString()))
            .andExpect(jsonPath("$.telefonSabit").value(DEFAULT_TELEFON_SABIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVeli() throws Exception {
        // Get the veli
        restVeliMockMvc.perform(get("/api/velis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVeli() throws Exception {
        // Initialize the database
        veliService.save(veli);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockVeliSearchRepository);

        int databaseSizeBeforeUpdate = veliRepository.findAll().size();

        // Update the veli
        Veli updatedVeli = veliRepository.findById(veli.getId()).get();
        // Disconnect from session so that the updates on updatedVeli are not directly saved in db
        em.detach(updatedVeli);
        updatedVeli
            .ad(UPDATED_AD)
            .soyad(UPDATED_SOYAD)
            .adres(UPDATED_ADRES)
            .meslegi(UPDATED_MESLEGI)
            .email(UPDATED_EMAIL)
            .telefonCep(UPDATED_TELEFON_CEP)
            .telefonSabit(UPDATED_TELEFON_SABIT);

        restVeliMockMvc.perform(put("/api/velis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVeli)))
            .andExpect(status().isOk());

        // Validate the Veli in the database
        List<Veli> veliList = veliRepository.findAll();
        assertThat(veliList).hasSize(databaseSizeBeforeUpdate);
        Veli testVeli = veliList.get(veliList.size() - 1);
        assertThat(testVeli.getAd()).isEqualTo(UPDATED_AD);
        assertThat(testVeli.getSoyad()).isEqualTo(UPDATED_SOYAD);
        assertThat(testVeli.getAdres()).isEqualTo(UPDATED_ADRES);
        assertThat(testVeli.getMeslegi()).isEqualTo(UPDATED_MESLEGI);
        assertThat(testVeli.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testVeli.getTelefonCep()).isEqualTo(UPDATED_TELEFON_CEP);
        assertThat(testVeli.getTelefonSabit()).isEqualTo(UPDATED_TELEFON_SABIT);

        // Validate the Veli in Elasticsearch
        verify(mockVeliSearchRepository, times(1)).save(testVeli);
    }

    @Test
    @Transactional
    public void updateNonExistingVeli() throws Exception {
        int databaseSizeBeforeUpdate = veliRepository.findAll().size();

        // Create the Veli

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeliMockMvc.perform(put("/api/velis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veli)))
            .andExpect(status().isBadRequest());

        // Validate the Veli in the database
        List<Veli> veliList = veliRepository.findAll();
        assertThat(veliList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Veli in Elasticsearch
        verify(mockVeliSearchRepository, times(0)).save(veli);
    }

    @Test
    @Transactional
    public void deleteVeli() throws Exception {
        // Initialize the database
        veliService.save(veli);

        int databaseSizeBeforeDelete = veliRepository.findAll().size();

        // Delete the veli
        restVeliMockMvc.perform(delete("/api/velis/{id}", veli.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Veli> veliList = veliRepository.findAll();
        assertThat(veliList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Veli in Elasticsearch
        verify(mockVeliSearchRepository, times(1)).deleteById(veli.getId());
    }

    @Test
    @Transactional
    public void searchVeli() throws Exception {
        // Initialize the database
        veliService.save(veli);
        when(mockVeliSearchRepository.search(queryStringQuery("id:" + veli.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(veli), PageRequest.of(0, 1), 1));
        // Search the veli
        restVeliMockMvc.perform(get("/api/_search/velis?query=id:" + veli.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veli.getId().intValue())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)))
            .andExpect(jsonPath("$.[*].soyad").value(hasItem(DEFAULT_SOYAD)))
            .andExpect(jsonPath("$.[*].adres").value(hasItem(DEFAULT_ADRES)))
            .andExpect(jsonPath("$.[*].meslegi").value(hasItem(DEFAULT_MESLEGI)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefonCep").value(hasItem(DEFAULT_TELEFON_CEP)))
            .andExpect(jsonPath("$.[*].telefonSabit").value(hasItem(DEFAULT_TELEFON_SABIT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Veli.class);
        Veli veli1 = new Veli();
        veli1.setId(1L);
        Veli veli2 = new Veli();
        veli2.setId(veli1.getId());
        assertThat(veli1).isEqualTo(veli2);
        veli2.setId(2L);
        assertThat(veli1).isNotEqualTo(veli2);
        veli1.setId(null);
        assertThat(veli1).isNotEqualTo(veli2);
    }
}
