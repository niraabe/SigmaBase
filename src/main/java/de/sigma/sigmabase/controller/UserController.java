package de.sigma.sigmabase.controller;

import de.sigma.sigmabase.controller.util.UserInputValidation;
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
import org.springframework.web.bind.annotation.PathVariable;
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

        mav.addObject("edituser", true);

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
            //If the user changed any information, the user object returned from the userService is != null
            if(user != null) {
                userService.login(user);
            }

            return this.userPage(pageable);
        } else {
            //User input validation crashed, editation round 2
            mav.addObject("edituser", true);
        }

        return mav;
    }

    /**
     * Request mapping to a specific user page
     *
     * @param username
     * @return
     */
    @RequestMapping("/user/{username}")
    public ModelAndView userPage(@PathVariable("username") String username) {
        LOG.info("Request GET to '/user/{}'", username);

        ModelAndView mav = new ModelAndView("user");

        //Do we have a login user
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        //Get the user we are searching for
        User user = userService.getUserByName(username);

        if (authenticated) {
            //get the login user
            User authUser = userService.getUser();

            if (authUser == null) {
                LOG.info("Couldnt locate session user while adding follower relation.");
                return indexController.index(new PageRequest(0, 5));
            }
            if (user == null) {
                LOG.info("Couldnt locate user with name: '{}'", username);
                return indexController.index(new PageRequest(0, 5));
            }

            //Check if it is myUserpage or a foreign user
            // Although check if we are already following this user
            if (authUser.equals(user)) {
                mav.addObject("myUserpage", true);
            } else {
                mav.addObject("foreignUserpageFollow", true);
            }

            mav.addObject("user", user);
        } else {
            LOG.info("Unauthorized user detected at userPage looking for user with name: '{}'", username);
            return indexController.index(new PageRequest(0, 5));
        }
        return mav;
    }

}
