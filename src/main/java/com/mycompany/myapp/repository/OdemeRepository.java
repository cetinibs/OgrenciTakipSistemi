package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Odeme;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Odeme entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OdemeRepository extends JpaRepository<Odeme, Long> {

}
