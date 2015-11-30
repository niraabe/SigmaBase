package de.sigma.sigmabase.service;

import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.repository.RegistrationKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by:  nilsraabe
 * Date:        29.11.15
 * Time:        19:02
 * E-Mail:      satriani.vay@gmail.com
 */
@Service
public class RegistrationKeyService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationKeyService.class);

    @Autowired
    RegistrationKeyRepository registrationKeyRepository;

    /**
     * Get a specific key from the database by the key itself
     * @param keyAsString
     * @return
     */
    public RegistrationKey getRegistrationKey(String keyAsString) {
        RegistrationKey key = registrationKeyRepository.findByKey(keyAsString);
        LOG.debug("Returning following registration Key: {}", key);
        return key;
    }

}
