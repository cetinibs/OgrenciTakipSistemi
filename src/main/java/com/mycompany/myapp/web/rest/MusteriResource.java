package com.mycompany.myapp.web.rest;
import com.mycompany.myapp.domain.Musteri;
import com.mycompany.myapp.service.MusteriService;
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
 * REST controller for managing Musteri.
 */
@RestController
@RequestMapping("/api")
public class MusteriResource {

    private final Logger log = LoggerFactory.getLogger(MusteriResource.class);

    private static final String ENTITY_NAME = "musteri";

    private final MusteriService musteriService;

    public MusteriResource(MusteriService musteriService) {
        this.musteriService = musteriService;
    }

    /**
     * POST  /musteris : Create a new musteri.
     *
     * @param musteri the musteri to create
     * @return the ResponseEntity with status 201 (Created) and with body the new musteri, or with status 400 (Bad Request) if the musteri has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/musteris")
    public ResponseEntity<Musteri> createMusteri(@RequestBody Musteri musteri) throws URISyntaxException {
        log.debug("REST request to save Musteri : {}", musteri);
        if (musteri.getId() != null) {
            throw new BadRequestAlertException("A new musteri cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Musteri result = musteriService.save(musteri);
        return ResponseEntity.created(new URI("/api/musteris/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /musteris : Updates an existing musteri.
     *
     * @param musteri the musteri to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated musteri,
     * or with status 400 (Bad Request) if the musteri is not valid,
     * or with status 500 (Internal Server Error) if the musteri couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/musteris")
    public ResponseEntity<Musteri> updateMusteri(@RequestBody Musteri musteri) throws URISyntaxException {
        log.debug("REST request to update Musteri : {}", musteri);
        if (musteri.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Musteri result = musteriService.save(musteri);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, musteri.getId().toString()))
            .body(result);
    }

    /**
     * GET  /musteris : get all the musteris.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of musteris in body
     */
    @GetMapping("/musteris")
    public ResponseEntity<List<Musteri>> getAllMusteris(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Musteris");
        Page<Musteri> page;
        if (eagerload) {
            page = musteriService.findAllWithEagerRelationships(pageable);
        } else {
            page = musteriService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/musteris?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /musteris/:id : get the "id" musteri.
     *
     * @param id the id of the musteri to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the musteri, or with status 404 (Not Found)
     */
    @GetMapping("/musteris/{id}")
    public ResponseEntity<Musteri> getMusteri(@PathVariable Long id) {
        log.debug("REST request to get Musteri : {}", id);
        Optional<Musteri> musteri = musteriService.findOne(id);
        return ResponseUtil.wrapOrNotFound(musteri);
    }

    /**
     * DELETE  /musteris/:id : delete the "id" musteri.
     *
     * @param id the id of the musteri to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/musteris/{id}")
    public ResponseEntity<Void> deleteMusteri(@PathVariable Long id) {
        log.debug("REST request to delete Musteri : {}", id);
        musteriService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/musteris?query=:query : search for the musteri corresponding
     * to the query.
     *
     * @param query the query of the musteri search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/musteris")
    public ResponseEntity<List<Musteri>> searchMusteris(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Musteris for query {}", query);
        Page<Musteri> page = musteriService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/musteris");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
