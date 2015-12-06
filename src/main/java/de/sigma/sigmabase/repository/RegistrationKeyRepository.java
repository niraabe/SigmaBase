package de.sigma.sigmabase.repository;

import de.sigma.sigmabase.model.user.RegistrationKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:  nilsraabe
 * Date:        29.11.15
 * Time:        18:52
 * E-Mail:      satriani.vay@gmail.com
 */
public interface RegistrationKeyRepository extends CrudRepository<RegistrationKey, Long> {

    RegistrationKey findByKey(String key);

    @Query(value = "SELECT k FROM RegistrationKey AS k WHERE k.user IS NOT null ORDER BY k.creationdate DESC")
    Page<RegistrationKey> findUsedRegistrationKeys(Pageable pageable);

    @Query(value = "SELECT k FROM RegistrationKey AS k WHERE k.user IS null ORDER BY k.creationdate DESC")
    Page<RegistrationKey> findUnUsedRegistrationKeys(Pageable pageable);
}
