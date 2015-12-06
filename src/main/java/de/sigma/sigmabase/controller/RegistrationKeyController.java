package de.sigma.sigmabase.controller;

import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.model.user.UserRole;
import de.sigma.sigmabase.service.RegistrationKeyService;
import de.sigma.sigmabase.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Created by:  nilsraabe
 * Date:        06.12.15
 * Time:        21:44
 * E-Mail:      satriani.vay@gmail.com
 */
@Controller
public class RegistrationKeyController {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationKeyController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private IndexController indexController;

    @Autowired
    private AdminController adminController;

    @Autowired
    private RegistrationKeyService registrationKeyService;

    /**
     * Deletes a specific registration key
     *
     * @param keyid
     * @param pageable
     * @return
     */
    @Transactional
    @RequestMapping(value = "/admin/deletekey/{keyid}", method = RequestMethod.POST)
    public ModelAndView deleteKey(@PathVariable("keyid") long keyid, @PageableDefault(size = 6) Pageable pageable) {
        LOG.info("Request POST to '/admin/deleteKey/{}'", keyid);

        ModelAndView mav = new ModelAndView("/admin/registrationkeys");

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

        //The user is logged in and is in the admin role, delete the key !
        boolean deleteAction = registrationKeyService.deleteRegistrationKeyById(keyid);
        Validate.isTrue(deleteAction, String.format("Error while deleting registration key with id: %s", keyid));
        LOG.info("Deleted registration key, id: {}", keyid);

        return adminController.unusedKeys(pageable);
    }

}
