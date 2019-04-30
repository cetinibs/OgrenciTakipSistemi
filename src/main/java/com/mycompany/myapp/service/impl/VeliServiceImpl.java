package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.VeliService;
import com.mycompany.myapp.domain.Veli;
import com.mycompany.myapp.repository.VeliRepository;
import com.mycompany.myapp.repository.search.VeliSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Veli.
 */
@Service
@Transactional
public class VeliServiceImpl implements VeliService {

    private final Logger log = LoggerFactory.getLogger(VeliServiceImpl.class);

    private final VeliRepository veliRepository;

    private final VeliSearchRepository veliSearchRepository;

    public VeliServiceImpl(VeliRepository veliRepository, VeliSearchRepository veliSearchRepository) {
        this.veliRepository = veliRepository;
        this.veliSearchRepository = veliSearchRepository;
    }

    /**
     * Save a veli.
     *
     * @param veli the entity to save
     * @return the persisted entity
     */
    @Override
    public Veli save(Veli veli) {
        log.debug("Request to save Veli : {}", veli);
        Veli result = veliRepository.save(veli);
        veliSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the velis.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Veli> findAll(Pageable pageable) {
        log.debug("Request to get all Velis");
        return veliRepository.findAll(pageable);
    }


    /**
     * Get one veli by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Veli> findOne(Long id) {
        log.debug("Request to get Veli : {}", id);
        return veliRepository.findById(id);
    }

    /**
     * Delete the veli by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Veli : {}", id);
        veliRepository.deleteById(id);
        veliSearchRepository.deleteById(id);
    }

    /**
     * Search for the veli corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Veli> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Velis for query {}", query);
        return veliSearchRepository.search(queryStringQuery(query), pageable);    }
}
