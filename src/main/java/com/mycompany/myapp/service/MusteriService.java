package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Musteri;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Musteri.
 */
public interface MusteriService {

    /**
     * Save a musteri.
     *
     * @param musteri the entity to save
     * @return the persisted entity
     */
    Musteri save(Musteri musteri);

    /**
     * Get all the musteris.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Musteri> findAll(Pageable pageable);

    /**
     * Get all the Musteri with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<Musteri> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" musteri.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Musteri> findOne(Long id);

    /**
     * Delete the "id" musteri.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the musteri corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Musteri> search(String query, Pageable pageable);
}
