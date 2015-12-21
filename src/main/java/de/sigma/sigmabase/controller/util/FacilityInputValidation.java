package de.sigma.sigmabase.controller.util;

import de.sigma.sigmabase.model.Facility;
import de.sigma.sigmabase.service.FacilityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class holds the functionallity to check the user input to a facility
 *
 * Created by:  nilsraabe
 * Date:        21.12.15
 * Time:        21:04
 * E-Mail:      satriani.vay@gmail.com
 */
@ControllerAdvice
public class FacilityInputValidation {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityInputValidation.class);

    @Autowired
    private FacilityService facilityService;

    /**
     * Validates a given facility
     *
     *
     * @param mav
     * @param facility
     * @param forumThreadIdAsString
     * @return returns true if an error occured
     */
    public boolean checkFacility(ModelAndView mav, Facility facility, String forumThreadIdAsString) {

        boolean errorflag = false;

        //Check the forum thread url
        errorflag = validateAndSetForumUrl(mav, facility, forumThreadIdAsString, errorflag);

        if (validateFacilityName(mav, facility.getName())) {
            errorflag = true;
        }

        return errorflag;
    }

    /**
     * Validates and sets the forum thread id
     *
     * @param mav
     * @param facility
     * @param forumThreadIdAsString
     * @param errorflag
     * @return
     */
    private boolean validateAndSetForumUrl(ModelAndView mav, Facility facility, String forumThreadIdAsString, boolean errorflag) {

        if(forumThreadIdAsString != null && forumThreadIdAsString.length() > 0) {

            Integer forumID = null;

            //Url is like:
            // http://forum.team-sigma.de/index.php/Thread/187-Weimarer-Archiv-2014/
            String pattern = "(.*forum\\.team-sigma\\.de/index\\.php/Thread/)(\\d+)(\\-.*)";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(forumThreadIdAsString);

            if (m.find()) {
                LOG.info("Parsed Thread id '{}' from string: '{}'", m.group(2), forumThreadIdAsString);
                forumID = Integer.parseInt(m.group(2));
                facility.setForumThreadId(forumID);
            } else {
                LOG.warn("Couldn't find thread ID in url: '{}'", forumThreadIdAsString);
                mav.addObject("forumThreadIdError", true);
                errorflag = true;
            }
        }

        return errorflag;
    }

    /**
     * Validates a facility name
     *
     *
     * @param mav
     * @param facilityName
     * @return true if an error occured
     */
    private boolean validateFacilityName(ModelAndView mav, String facilityName) {
        if (facilityName == null || facilityName.length() == 0) {
            LOG.info("User input for facilityName is null or length == 0 !");
            mav.addObject("facilityNameLength", true);
            return true;
        } else if (facilityName.matches("[a-zA-Z0-9/-/_öäüß/ ]+") == false) {
            LOG.info("User input for facilityName contains none valid characters! value: '{}'", facilityName);
            mav.addObject("facilityNameInvalid", true);
            return true;
        } else if (facilityService.getFacilityByName(facilityName) != null) {
            LOG.info("User input for facilityName is already taken! value: '{}'", facilityName);
            mav.addObject("facilityNameDuplicate", true);
            return true;
        } else if (facilityName.length() > 200) {
            LOG.info("User input for facilityName is to long! value: '{}'", facilityName);
            mav.addObject("facilityNameToLong", true);
            return true;
        }
        return false;
    }
}
