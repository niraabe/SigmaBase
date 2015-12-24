package de.sigma.sigmabase.controller;

import de.sigma.sigmabase.controller.util.FacilityInputValidation;
import de.sigma.sigmabase.model.Facility;
import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.model.user.UserRole;
import de.sigma.sigmabase.service.FacilityService;
import de.sigma.sigmabase.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by:  nilsraabe
 * Date:        12.12.15
 * Time:        22:28
 * E-Mail:      satriani.vay@gmail.com
 */
@Controller
public class FacilityController {

    private static final Logger LOG = LoggerFactory.getLogger(FacilityController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private IndexController indexController;

    @Autowired
    private FacilityInputValidation facilityInputValidation;
    /**
     * Shows all facilitys
     *
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/facilitys", method = RequestMethod.GET)
    public ModelAndView showAllFacilitys(@PageableDefault(size = 5) Pageable pageable) {
        LOG.info("Request GET to '/facilitys'");

        ModelAndView mav = new ModelAndView("facilitys/facilitys");
        mav.addObject("resolvingUrl", "facilitys");

        //Do we have a logged in user ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        if (authenticated) {
            //Get the session user
            User user = userService.getUser();
            mav.addObject("user", user);

            //Is the user in the role to do this ?
            if (user.getUserRole().ordinal() <  UserRole.READ.ordinal()) {
                LOG.warn("Unauthorized Request GET to '/facilitys' by user: {}", user);
                return indexController.index(new PageRequest(0, 5));
            }
        } else {
            LOG.warn("Unauthorized Request GET to '/facilitys' by not logged in user");
            return indexController.index(new PageRequest(0, 5));
        }

        //User is logged in and has the permission to view the facilitys

        //Get all facilitys
        Page<Facility> page = facilityService.getAllFacilitys(pageable);
        mav.addObject("page", page);
        mav.addObject("facilitys", page);

        return mav;
    }

    /**
     * Method to create a facility formular to add a facility
     *
     * @return
     */
    @RequestMapping(value = "/addfacility", method = RequestMethod.GET)
    public ModelAndView showFacilityFormular() {
        LOG.info("Request GET to '/addfacility'");

        ModelAndView mav = new ModelAndView("facilitys/addFacility");
        mav.addObject("resolvingUrl", "/addfacility");

        //Do we have a logged in user ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        if (authenticated) {
            //Get the session user
            User user = userService.getUser();
            mav.addObject("user", user);

            //Is the user in the role to do this ?
            if (user.getUserRole().ordinal() <  UserRole.READ.ordinal()) {
                LOG.warn("Unauthorized Request GET to '/addfacility' by user: {}", user);
                return indexController.index(new PageRequest(0, 5));
            }
        } else {
            LOG.warn("Unauthorized Request GET to '/addfacility' by not logged in user");
            return indexController.index(new PageRequest(0, 5));
        }

        //User is logged in and has the permission to add a facilitys
        mav.addObject("facility", new Facility());

        return mav;
    }

    /**
     * Method to add a new facility to the db
     *
     * @return
     */
    @RequestMapping(value = "/addfacility", method = RequestMethod.POST)
    public ModelAndView addFacility(@ModelAttribute("facility") @Valid Facility facility, @ModelAttribute("forumThreadIdAsString") String forumThreadIdAsString,BindingResult bindingResult) {
        LOG.info("Request POST to '/addfacility'");

        ModelAndView mav = new ModelAndView("facilitys/addFacility");

        //Do we have a logged in user ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        if (authenticated) {
            //Get the session user
            User user = userService.getUser();
            mav.addObject("user", user);

            //Is the user in the role to do this ?
            if (user.getUserRole().ordinal() <  UserRole.READ.ordinal()) {
                LOG.warn("Unauthorized Request POST to '/addfacility' by user: {}", user);
                return indexController.index(new PageRequest(0, 5));
            }
        } else {
            LOG.warn("Unauthorized Request POST to '/addfacility' by not logged in user");
            return indexController.index(new PageRequest(0, 5));
        }

        //User is logged in and has the permission to add a facilitys
        if(facilityInputValidation.checkFacility(mav, facility, forumThreadIdAsString) || bindingResult.hasErrors()) {
            return mav;
        }

        facilityService.addFacility(facility);

        return showAllFacilitys(new PageRequest(0, 5));
    }


}
