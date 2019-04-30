package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Veli;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Veli entity.
 */
public interface VeliSearchRepository extends ElasticsearchRepository<Veli, Long> {
}
