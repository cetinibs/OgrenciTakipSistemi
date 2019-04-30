package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.OdemeService;
import com.mycompany.myapp.domain.Odeme;
import com.mycompany.myapp.repository.OdemeRepository;
import com.mycompany.myapp.repository.search.OdemeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Odeme.
 */
@Service
@Transactional
public class OdemeServiceImpl implements OdemeService {

    private final Logger log = LoggerFactory.getLogger(OdemeServiceImpl.class);

    private final OdemeRepository odemeRepository;

    private final OdemeSearchRepository odemeSearchRepository;

    public OdemeServiceImpl(OdemeRepository odemeRepository, OdemeSearchRepository odemeSearchRepository) {
        this.odemeRepository = odemeRepository;
        this.odemeSearchRepository = odemeSearchRepository;
    }

    /**
     * Save a odeme.
     *
     * @param odeme the entity to save
     * @return the persisted entity
     */
    @Override
    public Odeme save(Odeme odeme) {
        log.debug("Request to save Odeme : {}", odeme);
        Odeme result = odemeRepository.save(odeme);
        odemeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the odemes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Odeme> findAll(Pageable pageable) {
        log.debug("Request to get all Odemes");
        return odemeRepository.findAll(pageable);
    }


    /**
     * Get one odeme by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Odeme> findOne(Long id) {
        log.debug("Request to get Odeme : {}", id);
        return odemeRepository.findById(id);
    }

    /**
     * Delete the odeme by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Odeme : {}", id);
        odemeRepository.deleteById(id);
        odemeSearchRepository.deleteById(id);
    }

    /**
     * Search for the odeme corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Odeme> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Odemes for query {}", query);
        return odemeSearchRepository.search(queryStringQuery(query), pageable);    }
}
