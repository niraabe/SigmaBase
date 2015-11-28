package de.sigma.sigmabase.controller;

import de.sigma.sigmabase.controller.validation.UserInputValidation;
import de.sigma.sigmabase.model.User;
import de.sigma.sigmabase.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller to handle a user registration
 *
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

    /**
     * Get request for the register page
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegistration() {
        LOG.info("Request GET to '/register'");
        return new ModelAndView("register", "user", new User());
    }

    /**
     * Request mapping for register a new user and store it in the db
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(@ModelAttribute User user, BindingResult result) {
        LOG.info("Request POST to '/register'");

        ModelAndView mav = new ModelAndView("register");

        //Validating the user input
        if (validation.validateUser(mav, user) == false) {

            user = userService.registerUser(user);
            userService.login(user);

            return indexController.index(new PageRequest(0, 5));
        }

        return mav;
    }

}
