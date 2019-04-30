package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Musteri;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Musteri entity.
 */
public interface MusteriSearchRepository extends ElasticsearchRepository<Musteri, Long> {
}
