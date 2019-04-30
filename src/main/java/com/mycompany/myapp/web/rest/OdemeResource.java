package com.mycompany.myapp.web.rest;
import com.mycompany.myapp.domain.Odeme;
import com.mycompany.myapp.service.OdemeService;
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
 * REST controller for managing Odeme.
 */
@RestController
@RequestMapping("/api")
public class OdemeResource {

    private final Logger log = LoggerFactory.getLogger(OdemeResource.class);

    private static final String ENTITY_NAME = "odeme";

    private final OdemeService odemeService;

    public OdemeResource(OdemeService odemeService) {
        this.odemeService = odemeService;
    }

    /**
     * POST  /odemes : Create a new odeme.
     *
     * @param odeme the odeme to create
     * @return the ResponseEntity with status 201 (Created) and with body the new odeme, or with status 400 (Bad Request) if the odeme has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/odemes")
    public ResponseEntity<Odeme> createOdeme(@RequestBody Odeme odeme) throws URISyntaxException {
        log.debug("REST request to save Odeme : {}", odeme);
        if (odeme.getId() != null) {
            throw new BadRequestAlertException("A new odeme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Odeme result = odemeService.save(odeme);
        return ResponseEntity.created(new URI("/api/odemes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /odemes : Updates an existing odeme.
     *
     * @param odeme the odeme to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated odeme,
     * or with status 400 (Bad Request) if the odeme is not valid,
     * or with status 500 (Internal Server Error) if the odeme couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/odemes")
    public ResponseEntity<Odeme> updateOdeme(@RequestBody Odeme odeme) throws URISyntaxException {
        log.debug("REST request to update Odeme : {}", odeme);
        if (odeme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Odeme result = odemeService.save(odeme);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, odeme.getId().toString()))
            .body(result);
    }

    /**
     * GET  /odemes : get all the odemes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of odemes in body
     */
    @GetMapping("/odemes")
    public ResponseEntity<List<Odeme>> getAllOdemes(Pageable pageable) {
        log.debug("REST request to get a page of Odemes");
        Page<Odeme> page = odemeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/odemes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /odemes/:id : get the "id" odeme.
     *
     * @param id the id of the odeme to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the odeme, or with status 404 (Not Found)
     */
    @GetMapping("/odemes/{id}")
    public ResponseEntity<Odeme> getOdeme(@PathVariable Long id) {
        log.debug("REST request to get Odeme : {}", id);
        Optional<Odeme> odeme = odemeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(odeme);
    }

    /**
     * DELETE  /odemes/:id : delete the "id" odeme.
     *
     * @param id the id of the odeme to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/odemes/{id}")
    public ResponseEntity<Void> deleteOdeme(@PathVariable Long id) {
        log.debug("REST request to delete Odeme : {}", id);
        odemeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/odemes?query=:query : search for the odeme corresponding
     * to the query.
     *
     * @param query the query of the odeme search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/odemes")
    public ResponseEntity<List<Odeme>> searchOdemes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Odemes for query {}", query);
        Page<Odeme> page = odemeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/odemes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
