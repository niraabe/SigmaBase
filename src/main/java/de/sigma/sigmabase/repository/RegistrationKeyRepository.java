package de.sigma.sigmabase.repository;

import de.sigma.sigmabase.model.user.RegistrationKey;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by:  nilsraabe
 * Date:        29.11.15
 * Time:        18:52
 * E-Mail:      satriani.vay@gmail.com
 */
public interface RegistrationKeyRepository extends CrudRepository<RegistrationKey, Long> {

    RegistrationKey findByKey(String key);

}
