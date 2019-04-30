package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Veli;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Veli.
 */
public interface VeliService {

    /**
     * Save a veli.
     *
     * @param veli the entity to save
     * @return the persisted entity
     */
    Veli save(Veli veli);

    /**
     * Get all the velis.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Veli> findAll(Pageable pageable);


    /**
     * Get the "id" veli.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Veli> findOne(Long id);

    /**
     * Delete the "id" veli.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the veli corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Veli> search(String query, Pageable pageable);
}
