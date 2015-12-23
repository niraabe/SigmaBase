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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
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
            //For registration key generation - thymleaf/form needs it
            mav.addObject("key", new RegistrationKey());

            //Is the user in the role ADMIN ?
            if (user.getUserRole() != UserRole.ADMIN) {
                LOG.warn("Unauthorized Request GET to '/admin' by user: {}", user);
                return indexController.index(new PageRequest(0, 5));
            }
        } else {
            LOG.warn("Unauthorized Request GET to '/admin' by not logged in user");
            return indexController.index(new PageRequest(0, 5));
        }

        return mav;
    }

    @RequestMapping(value = "/admin/usedkeys", method = RequestMethod.GET)
    public ModelAndView usedKeysPage(@PageableDefault(size = 16) Pageable pageable) {
        LOG.info("Request GET to '/admin/usedkeys'");

        ModelAndView mav = new ModelAndView("admin/registrationkeys");

        //Do we have a logged in User ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        if (authenticated) {
            //Get the session user
            User user = userService.getUser();
            mav.addObject("user", user);
            //For registration key generation - thymleaf/form needs it
            mav.addObject("key", new RegistrationKey());

            //Is the user in the role ADMIN ?
            if (user.getUserRole() != UserRole.ADMIN) {
                LOG.warn("Unauthorized Request GET to '/admin/usedkeys' by user: {}", user);
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
        mav.addObject("deleteGranted", false);

        return mav;
    }

    @RequestMapping(value = "/admin/unusedkeys", method = RequestMethod.GET)
    public ModelAndView unusedKeysPage(@PageableDefault(size = 16) Pageable pageable) {
        LOG.info("Request GET to '/admin/unusedkeys'");

        ModelAndView mav = new ModelAndView("admin/registrationkeys");

        //Do we have a logged in User ?
        boolean authenticated = userService.isAuthenticated();
        mav.addObject("auth", authenticated);

        if (authenticated) {
            //Get the session user
            User user = userService.getUser();
            mav.addObject("user", user);
            //For registration key generation - thymleaf/form needs it
            mav.addObject("key", new RegistrationKey());

            //Is the user in the role ADMIN ?
            if (user.getUserRole() != UserRole.ADMIN) {
                LOG.warn("Unauthorized Request GET to '/admin/unusedkeys' by user: {}", user);
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
        mav.addObject("deleteGranted", true);

        return mav;
    }

}
