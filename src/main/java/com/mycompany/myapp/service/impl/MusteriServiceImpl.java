package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.MusteriService;
import com.mycompany.myapp.domain.Musteri;
import com.mycompany.myapp.repository.MusteriRepository;
import com.mycompany.myapp.repository.search.MusteriSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Musteri.
 */
@Service
@Transactional
public class MusteriServiceImpl implements MusteriService {

    private final Logger log = LoggerFactory.getLogger(MusteriServiceImpl.class);

    private final MusteriRepository musteriRepository;

    private final MusteriSearchRepository musteriSearchRepository;

    public MusteriServiceImpl(MusteriRepository musteriRepository, MusteriSearchRepository musteriSearchRepository) {
        this.musteriRepository = musteriRepository;
        this.musteriSearchRepository = musteriSearchRepository;
    }

    /**
     * Save a musteri.
     *
     * @param musteri the entity to save
     * @return the persisted entity
     */
    @Override
    public Musteri save(Musteri musteri) {
        log.debug("Request to save Musteri : {}", musteri);
        Musteri result = musteriRepository.save(musteri);
        musteriSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the musteris.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Musteri> findAll(Pageable pageable) {
        log.debug("Request to get all Musteris");
        return musteriRepository.findAll(pageable);
    }

    /**
     * Get all the Musteri with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Musteri> findAllWithEagerRelationships(Pageable pageable) {
        return musteriRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one musteri by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Musteri> findOne(Long id) {
        log.debug("Request to get Musteri : {}", id);
        return musteriRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the musteri by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Musteri : {}", id);
        musteriRepository.deleteById(id);
        musteriSearchRepository.deleteById(id);
    }

    /**
     * Search for the musteri corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Musteri> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Musteris for query {}", query);
        return musteriSearchRepository.search(queryStringQuery(query), pageable);    }
}
