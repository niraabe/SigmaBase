package de.sigma.sigmabase.controller;

import de.sigma.sigmabase.controller.util.RegistrationKeyGenerator;
import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.model.user.UserRole;
import de.sigma.sigmabase.service.RegistrationKeyService;
import de.sigma.sigmabase.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by:  nilsraabe
 * Date:        30.11.15
 * Time:        20:30
 * E-Mail:      satriani.vay@gmail.com
 */
@Controller
public class AdminController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private IndexController indexController;

    @Autowired
    private RegistrationKeyGenerator registrationKeyGenerator;

    @Autowired
    private RegistrationKeyService registrationKeyService;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView adminPage() {
        LOG.info("Request GET to '/admin'");

        ModelAndView mav = new ModelAndView("admin/admin");

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

        return mav;
    }

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
                LOG.warn("User wasn't i9n the ADMIN role !");
                return indexController.index(new PageRequest(0, 5));
            }
        } else {
            LOG.warn("No logged in user wanted to generate registration key !");
            return indexController.index(new PageRequest(0, 5));
        }

        //user has the permission, go ahead
        String keyAsString = registrationKeyGenerator.generatePassword();
        key.setKey(keyAsString);

        //Set the new key in the model to show it to the admin
        mav.addObject("generatedKey", key);

        registrationKeyService.saveRegistrationKey(key);
        LOG.info("Generated and stored the following key: {}", key);

        return mav;
    }

    @RequestMapping(value = "/admin/usedkeys", method = RequestMethod.GET)
    public ModelAndView usedKeys(@PageableDefault(size = 6) Pageable pageable) {
        LOG.info("Request GET to '/admin/usedkeys'");

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

        //The user is logged in and is in the admin role, setup content
        Page<RegistrationKey> usedRegistrationKeys = registrationKeyService.getUsedRegistrationKeys(pageable);
        mav.addObject("keys", usedRegistrationKeys);
        mav.addObject("page", usedRegistrationKeys);
        mav.addObject("totalKeySize", usedRegistrationKeys.getTotalElements());
        mav.addObject("resolvingUrl", "/admin/usedkeys");

        return mav;
    }

    @RequestMapping(value = "/admin/unusedkeys", method = RequestMethod.GET)
    public ModelAndView unusedKeys(@PageableDefault(size = 6) Pageable pageable) {
        LOG.info("Request GET to '/admin/unusedkeys'");

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

        //The user is logged in and is in the admin role, setup content
        Page<RegistrationKey> usedRegistrationKeys = registrationKeyService.getUnUsedRegistrationKeys(pageable);
        mav.addObject("keys", usedRegistrationKeys);
        mav.addObject("page", usedRegistrationKeys);
        mav.addObject("totalKeySize", usedRegistrationKeys.getTotalElements());
        mav.addObject("resolvingUrl", "/admin/unusedkeys");

        return mav;
    }

}
