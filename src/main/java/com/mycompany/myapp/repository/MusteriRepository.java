package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Musteri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Musteri entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MusteriRepository extends JpaRepository<Musteri, Long> {

    @Query(value = "select distinct musteri from Musteri musteri left join fetch musteri.velis",
        countQuery = "select count(distinct musteri) from Musteri musteri")
    Page<Musteri> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct musteri from Musteri musteri left join fetch musteri.velis")
    List<Musteri> findAllWithEagerRelationships();

    @Query("select musteri from Musteri musteri left join fetch musteri.velis where musteri.id =:id")
    Optional<Musteri> findOneWithEagerRelationships(@Param("id") Long id);

}
