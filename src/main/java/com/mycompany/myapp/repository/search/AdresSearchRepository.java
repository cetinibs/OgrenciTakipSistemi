package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Adres;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Adres entity.
 */
public interface AdresSearchRepository extends ElasticsearchRepository<Adres, Long> {
}
