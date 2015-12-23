package de.sigma.sigmabase.controller;

import de.sigma.sigmabase.controller.util.UserInputValidation;
import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.service.RegistrationKeyService;
import de.sigma.sigmabase.service.UserService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to handle a user registration
 * <p/>
 * <p/>
 * Created by:  nilsraabe
 * Date:        18.06.15
 * Time:        22:56
 * E-Mail:      satriani.vay@gmail.com
 */
@Controller
public class RegisterController {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private IndexController indexController;

    @Autowired
    private UserInputValidation validation;

    @Autowired
    RegistrationKeyService registrationKeyService;

    /**
     * Get request for the register page
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegistration() {
        LOG.info("Request GET to '/register'");
        ModelMap map = new ModelMap();
        map.put("user", new User());
        map.put("key", "");
        map.put("edituser", false);

        return new ModelAndView("register", map);
    }

    /**
     * Request mapping for register a new user and store it in the db
     *
     * @param user
     * @param keyAsString
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute("user") User user, @ModelAttribute("key") String keyAsString) {
        LOG.info("Request POST to '/register'");

        ModelAndView mav = new ModelAndView("register");
        mav.addObject("edituser", false);

        //Validating the user input
        if (validation.validateUser(mav, user) == false) {

            Validate.isTrue(user.getRegistrationKey() == null, "Registrationkey of user to register isn't null, user: {} ", user);

            //Get the registration key from the database
            RegistrationKey registrationKey = registrationKeyService.getRegistrationKeyByKey(keyAsString);
            if (registrationKey == null || registrationKey.getUser() != null) {
                LOG.warn("Registrationkey error ! keyAsString: {}, user: {}", keyAsString, user);
                return new ModelAndView("errorRegistrationKey");
            }
            LOG.info("Registrationkey found in db ! key: {}", registrationKey);

            registrationKey.setUser(user);
            user.setRegistrationKey(registrationKey);
            user.setUserRole(registrationKey.getUserRole());
            user = userService.registerUser(user);
            userService.login(user);

            return indexController.index(new PageRequest(0, 5));
        }
        return mav;
    }
}
