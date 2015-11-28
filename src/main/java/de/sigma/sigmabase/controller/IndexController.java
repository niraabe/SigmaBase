package de.sigma.sigmabase.controller;

import de.sigma.sigmabase.model.Facility;
import de.sigma.sigmabase.model.User;
import de.sigma.sigmabase.service.FacilityService;
import de.sigma.sigmabase.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to handle requests to the index page
 * <p/>
 * Created by:  nilsraabe
 * Date:        07.06.15
 * Time:        20:06
 * E-Mail:      satriani.vay@gmail.com
 */
@Controller
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FacilityService facilityService;

    /**
     * Request mapping for the index page
     *
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(@PageableDefault(size = 5) Pageable pageable) {
        LOG.info("Request GET to '/index'");

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("resolvingUrl", "/");

        //Do we have a logged in user ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        if (authenticated) {
            //Get the logged in user
            User user = userService.getUser();
            mav.addObject("user", user);

            //Get all facilitys
            Page<Facility> page = facilityService.getAllFacilitys(pageable);
            mav.addObject("page", page);
            mav.addObject("facilitys", page);
        }

        return mav;
    }

}
