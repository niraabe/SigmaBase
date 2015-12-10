package de.sigma.sigmabase.controller.admin;

import de.sigma.sigmabase.controller.IndexController;
import de.sigma.sigmabase.controller.util.RegistrationKeyGenerator;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.Validate;

/**
 * Controller top handle requests by registration keys from the admin area
 *
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

    @Autowired
    private RegistrationKeyGenerator registrationKeyGenerator;

    /**
     * Adds a unique registration key to the database
     *
     * @param key
     * @return
     */
    @Transactional
    @RequestMapping(value = "/admin/addregistrationkey", method = RequestMethod.POST)
    public ModelAndView addRegistrationKey(@ModelAttribute("key") RegistrationKey key) {
        LOG.info("Request POST to '/admin/addregistrationkey'");

        ModelAndView mav = new ModelAndView("admin/admin");

        //Do we have a logged in User ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        if (authenticated) {
            //Get the session user
            User user = userService.getUser();
            mav.addObject("user", user);
            LOG.info("Following user wants to generate registration key: {}", user);

            //Is the user in the role ADMIN ?
            if (user.getUserRole() != UserRole.ADMIN) {
                LOG.warn("Unauthorized Request GET to '/admin/addregistrationkey' by user: {}", user);
                return indexController.index(new PageRequest(0, 5));
            }
        } else {
            LOG.warn("No logged in user wanted to generate registration key !");
            return indexController.index(new PageRequest(0, 5));
        }

        //No user role option set by user
        if (key.getUserRole() == null) {
            mav.addObject("error_role", true);
            return mav;
        }

        //user has the permission, go ahead

        //Generate a unique registration key which is not a duplicate
        String keyAsString;
        do {
            keyAsString = registrationKeyGenerator.generatePassword();
        } while (registrationKeyService.getRegistrationKeyByKey(keyAsString) != null);
        key.setKey(keyAsString);

        //Set the new key in the model to show it to the admin
        mav.addObject("generatedKey", key);

        registrationKeyService.saveRegistrationKey(key);
        LOG.info("Generated and stored the following key: {}", key);

        return mav;
    }

    /**
     * Deletes a specific registration key
     *
     * @param keyid
     * @param pageable
     * @return
     */
    @Transactional
    @RequestMapping(value = "/admin/deletekey/{keyid}", method = RequestMethod.POST)
    public ModelAndView deleteRegistrationKey(@PathVariable("keyid") long keyid, @PageableDefault(size = 16) Pageable pageable) {
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
                LOG.warn("Unauthorized Request POST to '/admin/deleteKey/{}' by user: {}", keyid, user);
                return indexController.index(new PageRequest(0, 16));
            }
        } else {
            LOG.warn("No logged in user wanted to deleteregistration key by id: {}!", keyid);
            return indexController.index(new PageRequest(0, 16));
        }

        //The user is logged in and is in the admin role, delete the key !
        boolean deleteAction = registrationKeyService.deleteRegistrationKeyById(keyid);
        Validate.isTrue(deleteAction, String.format("Error while deleting registration key with id: %s", keyid));
        LOG.info("Deleted registration key, id: {}", keyid);

        int pageNumber = pageable.getPageNumber();
        PageRequest pageRequest = new PageRequest(pageNumber, 16);

        return adminController.unusedKeysPage(pageRequest);
    }

}
