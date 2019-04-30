package com.mycompany.myapp.web.rest;
import com.mycompany.myapp.domain.Veli;
import com.mycompany.myapp.service.VeliService;
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
 * REST controller for managing Veli.
 */
@RestController
@RequestMapping("/api")
public class VeliResource {

    private final Logger log = LoggerFactory.getLogger(VeliResource.class);

    private static final String ENTITY_NAME = "veli";

    private final VeliService veliService;

    public VeliResource(VeliService veliService) {
        this.veliService = veliService;
    }

    /**
     * POST  /velis : Create a new veli.
     *
     * @param veli the veli to create
     * @return the ResponseEntity with status 201 (Created) and with body the new veli, or with status 400 (Bad Request) if the veli has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/velis")
    public ResponseEntity<Veli> createVeli(@RequestBody Veli veli) throws URISyntaxException {
        log.debug("REST request to save Veli : {}", veli);
        if (veli.getId() != null) {
            throw new BadRequestAlertException("A new veli cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Veli result = veliService.save(veli);
        return ResponseEntity.created(new URI("/api/velis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /velis : Updates an existing veli.
     *
     * @param veli the veli to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated veli,
     * or with status 400 (Bad Request) if the veli is not valid,
     * or with status 500 (Internal Server Error) if the veli couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/velis")
    public ResponseEntity<Veli> updateVeli(@RequestBody Veli veli) throws URISyntaxException {
        log.debug("REST request to update Veli : {}", veli);
        if (veli.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Veli result = veliService.save(veli);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, veli.getId().toString()))
            .body(result);
    }

    /**
     * GET  /velis : get all the velis.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of velis in body
     */
    @GetMapping("/velis")
    public ResponseEntity<List<Veli>> getAllVelis(Pageable pageable) {
        log.debug("REST request to get a page of Velis");
        Page<Veli> page = veliService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/velis");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /velis/:id : get the "id" veli.
     *
     * @param id the id of the veli to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the veli, or with status 404 (Not Found)
     */
    @GetMapping("/velis/{id}")
    public ResponseEntity<Veli> getVeli(@PathVariable Long id) {
        log.debug("REST request to get Veli : {}", id);
        Optional<Veli> veli = veliService.findOne(id);
        return ResponseUtil.wrapOrNotFound(veli);
    }

    /**
     * DELETE  /velis/:id : delete the "id" veli.
     *
     * @param id the id of the veli to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/velis/{id}")
    public ResponseEntity<Void> deleteVeli(@PathVariable Long id) {
        log.debug("REST request to delete Veli : {}", id);
        veliService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/velis?query=:query : search for the veli corresponding
     * to the query.
     *
     * @param query the query of the veli search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/velis")
    public ResponseEntity<List<Veli>> searchVelis(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Velis for query {}", query);
        Page<Veli> page = veliService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/velis");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
