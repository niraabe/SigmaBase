package de.sigma.sigmabase.controller;

import de.sigma.sigmabase.controller.validation.UserInputValidation;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to handle requests to user pages
 * <p/>
 * Created by:  nilsraabe
 * Date:        07.06.15
 * Time:        20:06
 * E-Mail:      satriani.vay@gmail.com
 */
@Controller
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private IndexController indexController;

    @Autowired
    private UserInputValidation validation;

    /**
     * Mapping to show userpage
     *
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView userPage(@PageableDefault(value = 5) Pageable pageable) {
        LOG.info("Request GET to '/user'");

        ModelAndView mav = new ModelAndView("user");

        //Do we have a logged in User ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);
        mav.addObject("myUserpage", true);

        if (authenticated) {
            //Get the session user
            User user = userService.getUser();
            mav.addObject("user", user);
        } else {
            return indexController.index(new PageRequest(0, 5));
        }

        return mav;
    }

    /**
     * Mapping to show user modification page
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView editUser() {
        LOG.info("Request GET to '/edit'");

        ModelAndView mav = new ModelAndView("editUser");

        //Do we have a logged in User ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        if (authenticated) {
            User user = userService.getUser();
            mav.addObject("user", user);
        } else {
            return indexController.index(new PageRequest(0, 5));
        }

        return mav;
    }

    /**
     * Mapping to submit the user modification
     *
     * @param editUser
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView submitEditUser(@ModelAttribute User editUser, @PageableDefault(value = 5) Pageable pageable) {
        LOG.info("Request POST to '/edit'");

        //Do we have a logged in User ?
        boolean authenticated = userService.isAuthenticated();
        User authUser;

        //Get and set the logged in user
        if (authenticated) {
            authUser = userService.getUser();
        } else {
            LOG.info("Unauthenticated user tried to edit user!");
            return indexController.index(new PageRequest(0, 5));
        }

        //Validate the user objects
        if (editUser == null) {
            LOG.info("Edited user object is null !");
            return indexController.index(new PageRequest(0, 5));
        }
        if (authUser == null) {
            LOG.info("Couldn't recieve authenticated user !");
            return indexController.index(new PageRequest(0, 5));
        }

        ModelAndView mav = new ModelAndView("editUser");

        //Validate the user input
        //If the validation succeeds, update the user in the db
        if (validation.validateEditUser(mav, authUser, editUser) == false) {
            User user = userService.updateUser(editUser, authUser);
            userService.login(user);

            return this.userPage(pageable);
        }

        return mav;
    }
}
