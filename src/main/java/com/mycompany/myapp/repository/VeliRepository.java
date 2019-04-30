package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Veli;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Veli entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VeliRepository extends JpaRepository<Veli, Long> {

}
