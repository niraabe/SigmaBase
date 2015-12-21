package de.sigma.sigmabase.controller.admin;

import de.sigma.sigmabase.controller.IndexController;
import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.model.user.UserRole;
import de.sigma.sigmabase.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.Validate;

/**
 * Controller to handle requests to the user page of the admin area
 * <p/>
 * Created by:  nilsraabe
 * Date:        10.12.15
 * Time:        21:25
 * E-Mail:      satriani.vay@gmail.com
 */
@Controller
public class AdminUserController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private IndexController indexController;

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public ModelAndView adminUsersPage(@PageableDefault(size = 16) Pageable pageable) {
        LOG.info("Request GET to '/admin/users'");

        ModelAndView mav = new ModelAndView("/admin/registeredusers");

        //Do we have a logged in User ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        if (authenticated) {
            //Get the session user
            User user = userService.getUser();
            mav.addObject("user", user);
            mav.addObject("key", new RegistrationKey());

            //Is the user in the role ADMIN ?
            if (user.getUserRole() != UserRole.ADMIN) {
                return indexController.index(new PageRequest(0, 5));
            }
        } else {
            return indexController.index(new PageRequest(0, 5));
        }

        //The user is logged in and is in the admin role, setup content
        Page<User> allUsers = userService.getAllUsers(pageable);
        mav.addObject("page", allUsers);
        mav.addObject("totalKeySize", allUsers.getTotalElements());

        return mav;
    }

    /**
     * Deletes a specific registration key
     *
     * @param userid
     * @param pageable
     * @return
     */
    @Transactional
    @RequestMapping(value = "/admin/deleteuser/{userid}", method = RequestMethod.POST)
    public ModelAndView deleteUser(@PathVariable("userid") long userid, @PageableDefault(size = 16) Pageable pageable) {
        LOG.info("Request POST to '/admin/deleteuser/{}'", userid);

        ModelAndView mav = new ModelAndView("/admin/users");

        //Do we have a logged in User ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        User user = userService.getUser();
        if (authenticated) {
            //Get the session user
            mav.addObject("user", user);
            mav.addObject("key", new RegistrationKey());

            //Is the user in the role ADMIN ?
            if (user.getUserRole() != UserRole.ADMIN) {
                return indexController.index(new PageRequest(0, 16));
            }
        } else {
            return indexController.index(new PageRequest(0, 16));
        }

        Validate.notNull(user, "User object shouldn't be null at this point !");
        User userByID = userService.getUserByID(userid);
        if (userByID.equals(user)) {
            LOG.info("User wanted to delete himself: user: {}", user);
            return this.adminUsersPage(pageable);
        }

        //The user is logged in and is in the admin role, delete the user !
        boolean deleteAction = userService.deleteUserById(userid);
        Validate.isTrue(deleteAction, String.format("Error while deleting user with id: %s", userid));
        LOG.info("Deleted user: {} \n Deletion by user: {}", userByID, user);

        int pageNumber = pageable.getPageNumber();
        PageRequest pageRequest = new PageRequest(pageNumber, 16);

        return this.adminUsersPage(pageRequest);
    }

}
