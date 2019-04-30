package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Odeme;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Odeme entity.
 */
public interface OdemeSearchRepository extends ElasticsearchRepository<Odeme, Long> {
}
