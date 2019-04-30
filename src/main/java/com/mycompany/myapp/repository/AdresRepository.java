package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Adres;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Adres entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdresRepository extends JpaRepository<Adres, Long> {

}
