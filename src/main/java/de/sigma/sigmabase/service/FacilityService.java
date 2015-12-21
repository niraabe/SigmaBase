package de.sigma.sigmabase.service;

import de.sigma.sigmabase.model.Facility;
import de.sigma.sigmabase.repository.FacilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by:  nilsraabe
 * Date:        28.11.15
 * Time:        23:01
 * E-Mail:      satriani.vay@gmail.com
 */
@Service
public class FacilityService {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityService.class);

    @Autowired
    private FacilityRepository facilityRepository;

    /**
     * Return all facilitys
     * Ordered Descending by name
     *
     * @param pageable used for pagination
     * @return
     */
    public Page<Facility> getAllFacilitys(Pageable pageable) {
        LOG.debug("Returning all facilitys page {}", pageable.getPageNumber());
        return facilityRepository.findAll(pageable);
    }

    /**
     * Adds / Updates a facility in the database
     *
     * @param facility
     * @return
     */
    public Facility addFacility(Facility facility) {
        Facility fac = facilityRepository.save(facility);
        LOG.debug("Adding facility to DB. Facility: {}", fac);
        return fac;
    }

    public Facility getFacilityByName(String facilityName) {
        Facility fac = facilityRepository.findByName(facilityName);
        LOG.debug("Found facility by name - facility: {}", fac);
        return fac;
    }
}
