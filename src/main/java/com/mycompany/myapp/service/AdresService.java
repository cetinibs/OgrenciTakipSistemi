package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Adres;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Adres.
 */
public interface AdresService {

    /**
     * Save a adres.
     *
     * @param adres the entity to save
     * @return the persisted entity
     */
    Adres save(Adres adres);

    /**
     * Get all the adres.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Adres> findAll(Pageable pageable);


    /**
     * Get the "id" adres.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Adres> findOne(Long id);

    /**
     * Delete the "id" adres.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the adres corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Adres> search(String query, Pageable pageable);
}
