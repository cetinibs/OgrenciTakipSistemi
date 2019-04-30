package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Odeme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Odeme.
 */
public interface OdemeService {

    /**
     * Save a odeme.
     *
     * @param odeme the entity to save
     * @return the persisted entity
     */
    Odeme save(Odeme odeme);

    /**
     * Get all the odemes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Odeme> findAll(Pageable pageable);


    /**
     * Get the "id" odeme.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Odeme> findOne(Long id);

    /**
     * Delete the "id" odeme.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the odeme corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Odeme> search(String query, Pageable pageable);
}
