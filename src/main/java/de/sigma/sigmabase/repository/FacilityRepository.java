package de.sigma.sigmabase.repository;

import de.sigma.sigmabase.model.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by:  nilsraabe
 * Date:        28.11.15
 * Time:        23:02
 * E-Mail:      satriani.vay@gmail.com
 */
public interface FacilityRepository extends CrudRepository<Facility, Long> {

    Page<Facility> findAll(Pageable pageable);

}
