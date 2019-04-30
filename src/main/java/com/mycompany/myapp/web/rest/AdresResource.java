package com.mycompany.myapp.web.rest;
import com.mycompany.myapp.domain.Adres;
import com.mycompany.myapp.service.AdresService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Adres.
 */
@RestController
@RequestMapping("/api")
public class AdresResource {

    private final Logger log = LoggerFactory.getLogger(AdresResource.class);

    private static final String ENTITY_NAME = "adres";

    private final AdresService adresService;

    public AdresResource(AdresService adresService) {
        this.adresService = adresService;
    }

    /**
     * POST  /adres : Create a new adres.
     *
     * @param adres the adres to create
     * @return the ResponseEntity with status 201 (Created) and with body the new adres, or with status 400 (Bad Request) if the adres has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/adres")
    public ResponseEntity<Adres> createAdres(@RequestBody Adres adres) throws URISyntaxException {
        log.debug("REST request to save Adres : {}", adres);
        if (adres.getId() != null) {
            throw new BadRequestAlertException("A new adres cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Adres result = adresService.save(adres);
        return ResponseEntity.created(new URI("/api/adres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adres : Updates an existing adres.
     *
     * @param adres the adres to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated adres,
     * or with status 400 (Bad Request) if the adres is not valid,
     * or with status 500 (Internal Server Error) if the adres couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/adres")
    public ResponseEntity<Adres> updateAdres(@RequestBody Adres adres) throws URISyntaxException {
        log.debug("REST request to update Adres : {}", adres);
        if (adres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Adres result = adresService.save(adres);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, adres.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adres : get all the adres.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of adres in body
     */
    @GetMapping("/adres")
    public ResponseEntity<List<Adres>> getAllAdres(Pageable pageable) {
        log.debug("REST request to get a page of Adres");
        Page<Adres> page = adresService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/adres");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /adres/:id : get the "id" adres.
     *
     * @param id the id of the adres to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the adres, or with status 404 (Not Found)
     */
    @GetMapping("/adres/{id}")
    public ResponseEntity<Adres> getAdres(@PathVariable Long id) {
        log.debug("REST request to get Adres : {}", id);
        Optional<Adres> adres = adresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adres);
    }

    /**
     * DELETE  /adres/:id : delete the "id" adres.
     *
     * @param id the id of the adres to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/adres/{id}")
    public ResponseEntity<Void> deleteAdres(@PathVariable Long id) {
        log.debug("REST request to delete Adres : {}", id);
        adresService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/adres?query=:query : search for the adres corresponding
     * to the query.
     *
     * @param query the query of the adres search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/adres")
    public ResponseEntity<List<Adres>> searchAdres(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Adres for query {}", query);
        Page<Adres> page = adresService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/adres");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
