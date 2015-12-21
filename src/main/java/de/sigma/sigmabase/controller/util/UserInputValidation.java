package de.sigma.sigmabase.controller.util;

import de.sigma.sigmabase.model.user.Gender;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

/**
 * "Controller" to validate the user input.
 * Each method takes a ModelAndView object to push
 * an error flag (like mav.addObject("usernameLength", true))
 * in it if an error exists.
 * <p/>
 * Created by:  nilsraabe
 * Date:        27.06.15
 * Time:        13:36
 * E-Mail:      satriani.vay@gmail.com
 */
@ControllerAdvice
public class UserInputValidation {

    private static final Logger LOG = LoggerFactory.getLogger(UserInputValidation.class);

    @Autowired
    private UserService userService;

    /**
     * Validating the user input for the username
     *
     * @param mav
     * @param username
     * @return
     */
    public boolean validateUsername(ModelAndView mav, String username) {
        if (username == null || username.length() == 0) {
            LOG.info("User input for username is null or length == 0 !");
            mav.addObject("usernameLength", true);
            return true;
        } else if (username.matches("[a-zA-Z0-9/-/_öäü]+") == false) {
            LOG.info("User input for username contains none valid characters! value: '{}'", username);
            mav.addObject("usernameInvalid", true);
            return true;
        } else if (userService.getUserByName(username) != null) {
            LOG.info("User input for username is already taken! value: '{}'", username);
            mav.addObject("usernameDuplicate", true);
            return true;
        } else if (username.length() > 50) {
            LOG.info("User input for username is to long! value: '{}'", username);
            mav.addObject("usernameToLong", true);
            return true;
        }
        return false;
    }

    /**
     * Validating the user input for the username - only by edit page
     *
     * @param mav
     * @param username
     */
    public boolean validateEditedUsername(ModelAndView mav, String username, String usernameEdited) {
        if (usernameEdited.equals(username) == false) {
            LOG.info("User edited username ! username: {}, username edited: {}", username, usernameEdited);
            mav.addObject("usernameLength", true);
            return true;
        }
        return false;
    }

    /**
     * Validating the user input for the password
     *
     * @param mav
     * @param password
     */
    public boolean validatePassword(ModelAndView mav, String password) {
        if (password == null || password.length() < 8) {
            LOG.info("User input for password is null or length <= 8 !");
            mav.addObject("passwordLength", true);
            return true;
        } else if (password.length() > 50) {
            LOG.info("User input for password is to long! value: '{}'", password);
            mav.addObject("passwordToLong", true);
            return true;
        }
        return false;
    }

    /**
     * Validating the user input for the new (edited) password
     *
     * @param mav
     * @param password
     */
    public boolean validateNewPassword(ModelAndView mav, String password) {
        if (password == null || password.length() < 8) {
            if (StringUtils.equals("", password) == false) {
                LOG.info("User input for password is null or length <= 8 !");
                mav.addObject("passwordLength", true);
                return true;
            }
        } else if (password.length() > 50) {
            LOG.info("User input for password is to long! value: '{}'", password);
            mav.addObject("passwordToLong", true);
            return true;
        }
        return false;
    }

    /**
     * Validating the user input for the email address
     *
     * @param mav
     * @param email
     */
    public boolean validateEmail(ModelAndView mav, String email) {
        if (email == null || email.length() == 0) {
            LOG.info("User input for email is null or length == 0 !");
            mav.addObject("emailLength", true);
            return true;
        } else if (email.matches("\\b[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}\\b") == false) {
            LOG.info("User input for email contains none valid characters! value: '{}'", email);
            mav.addObject("emailInvalid", true);
            return true;
        } else if (email.length() > 50) {
            LOG.info("User input for email is to long! value: '{}'", email);
            mav.addObject("emailToLong", true);
            return true;
        }
        return false;
    }

    /**
     * Validating the user input for the surname
     *
     * @param mav
     * @param surname
     */
    public boolean validateSurname(ModelAndView mav, String surname) {
        if (surname == null || surname.length() == 0) {
            LOG.info("User input for surname is null or length == 0 !");
            mav.addObject("surnameLength", true);
            return true;
        } else if (surname.matches("[a-zA-Z0-9/ öäü]+") == false) {
            LOG.info("User input for surname contains none valid characters! value: '{}'", surname);
            mav.addObject("surnameInvalid", true);
            return true;
        } else if (surname.length() > 50) {
            LOG.info("User input for surname is to long! value: '{}'", surname);
            mav.addObject("surnameToLong", true);
            return true;
        }
        return false;
    }

    /**
     * Validating the user input for the forename
     *
     * @param mav
     * @param forename
     */
    public boolean validateForename(ModelAndView mav, String forename) {
        if (forename == null || forename.length() == 0) {
            LOG.info("User input for forename is null or length == 0 !");
            mav.addObject("forenameLength", true);
            return true;
        } else if (forename.matches("[a-zA-Z0-9/ öäü]+") == false) {
            LOG.info("User input for forename contains none valid characters! value: '{}'", forename);
            mav.addObject("forenameInvalid", true);
            return true;
        } else if (forename.length() > 50) {
            LOG.info("User input for forename is to long! value: '{}'", forename);
            mav.addObject("forenameToLong", true);
            return true;
        }
        return false;
    }

    /**
     * Validating the user input for the description
     *
     * @param mav
     * @param description
     */
    public boolean validateDescription(ModelAndView mav, String description) {
        if (description == null || description.length() == 0) {
            LOG.info("User input for description is null or length == 0 !");
            mav.addObject("descriptionLength", true);
            return true;
        } else if (description.matches("[A-Za-z\\d\\s.,;:+*#'()!§?=/&%$\"öäü+-]+") == false) {
            LOG.info("User input for description contains none valid characters! value: '{}'", description);
            mav.addObject("descriptionInvalid", true);
            return true;
        } else if (description.length() > 1024) {
            LOG.info("User input for description is to long! value: '{}'", description);
            mav.addObject("descriptionToLong", true);
            return true;
        }
        return false;
    }

    /**
     * Validating the user input for a search term
     *
     * @param mav
     * @param searchterm
     */
    public boolean validateSearchTerm(ModelAndView mav, String searchterm) {
        if (searchterm == null || searchterm.length() == 0) {
            LOG.info("User input for search term is null or length == 0 !");
            mav.addObject("searchLength", true);
            return true;
        } else if (searchterm.matches("[A-Za-z\\d\\s.,;:+*#'()!§?=/&%$\"öäü+-]+") == false) {
            LOG.info("User input for search term contains none valid characters! value: '{}'", searchterm);
            mav.addObject("searchInvalid", true);
            return true;
        } else if (searchterm.length() > 1024) {
            LOG.info("User input for search term is to long! value: '{}'", searchterm);
            mav.addObject("searchToLong", true);
            return true;
        }
        return false;
    }

    /**
     * Validating user input for the gender
     *
     * @param mav
     * @param gender
     */
    public boolean validateGender(ModelAndView mav, Gender gender) {
        if (gender == null) {
            LOG.info("User input for gender is null !");
            mav.addObject("genderNull", true);
            return true;
        }
        return false;
    }

    /**
     * Validating user input for the birthday
     *
     * @param mav
     * @param birthday
     */
    public boolean validateBirthday(ModelAndView mav, DateTime birthday) {
        if (birthday == null) {
            LOG.info("User input for birthday is null !");
            mav.addObject("birthdayNull", true);
            return true;
        }
        return false;
    }

    /**
     * Validating user input for a chrip message/post
     *
     * @param message
     * @param mav
     * @return
     */
    public boolean validateMessage(String message, ModelAndView mav) {
        if (message == null || message.length() == 0) {
            LOG.info("Message is null or length == 0 ! value: '{}'", message);
            mav.addObject("messageToShort", true);
            return true;
        } else if (message.length() > 141) {
            LOG.info("Message has a length from '{}'. Should be < 141 !", message.length());
            mav.addObject("messageToLong", true);
            return true;
        } else if (message.matches("[A-Za-z\\d\\s.,;:+*#'()!§?=/&%$\"öäü+-]+") == false) {
            LOG.info("Message has invalid Characters ! value: '{}'", message);
            mav.addObject("messageInvalid", true);
            return true;
        }
        return false;
    }

    /**
     * Validates a given user object by the user input
     * Sets error parameter in the mav
     * returns true if error occurred
     *
     * @param mav
     * @param user
     * @return
     */
    public boolean validateUser(ModelAndView mav, User user) {

        boolean errorflag = false;

        if (validateUsername(mav, user.getUsername())) {
            errorflag = true;
        }
        if (validatePassword(mav, user.getPassword())) {
            errorflag = true;
        }
        if (validateForename(mav, user.getForename())) {
            errorflag = true;
        }
        if (validateSurname(mav, user.getSurname())) {
            errorflag = true;
        }
        if (validateBirthday(mav, user.getBirthday())) {
            errorflag = true;
        }
        if (validateGender(mav, user.getGender())) {
            errorflag = true;
        }
        if (validateDescription(mav, user.getDescription())) {
            errorflag = true;
        }
        if (validateEmail(mav, user.getEmail())) {
            errorflag = true;
        }
        return errorflag;
    }

    /**
     * Validates a given edited user object by the user input
     * Auth User is needed for Username validation
     * Sets error parameter in the mav
     * returns true if error occurred
     *
     * @param mav
     * @param authUser
     * @param editedUser
     * @return
     */
    public boolean validateEditUser(ModelAndView mav, User authUser, User editedUser) {

        boolean errorflag = false;

        if (validateEditedUsername(mav, authUser.getUsername(), editedUser.getUsername())) {
            errorflag = true;
        }
        if (validateNewPassword(mav, editedUser.getPassword())) {
            errorflag = true;
        }
        if (validateForename(mav, editedUser.getForename())) {
            errorflag = true;
        }
        if (validateSurname(mav, editedUser.getSurname())) {
            errorflag = true;
        }
        if (validateBirthday(mav, editedUser.getBirthday())) {
            errorflag = true;
        }
        if (validateGender(mav, editedUser.getGender())) {
            errorflag = true;
        }
        if (validateDescription(mav, editedUser.getDescription())) {
            errorflag = true;
        }
        if (validateEmail(mav, editedUser.getEmail())) {
            errorflag = true;
        }
        return errorflag;
    }
}
