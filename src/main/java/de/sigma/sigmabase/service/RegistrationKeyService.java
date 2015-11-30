package de.sigma.sigmabase.service;

import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.repository.RegistrationKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.Validate;

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
     *
     * @param keyAsString
     * @return
     */
    public RegistrationKey getRegistrationKey(String keyAsString) {
        RegistrationKey key = registrationKeyRepository.findByKey(keyAsString);
        LOG.debug("Returning following registration Key: {}", key);
        return key;
    }

    /**
     * Store the given registration key in the database
     *
     * @param registrationKey
     * @return
     */
    public RegistrationKey saveRegistrationKey(RegistrationKey registrationKey) {
        //Validate the key
        String keyAsString = registrationKey.getKey();
        Validate.notNull(keyAsString, "Registration key (String in obj) is null !");
        Validate.isTrue(keyAsString.length() == 1024, String.format("Registration key hasn't the length of 1024 ! key: %s",
                registrationKey));
        Validate.notNull(registrationKey.getUserRole(), "User role in registration key was null !");

        //Store the registration key
        RegistrationKey key = registrationKeyRepository.save(registrationKey);
        LOG.debug("Stored the following registration Key in DB: {}", key);
        return key;
    }

}
