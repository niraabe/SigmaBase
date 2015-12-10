package de.sigma.sigmabase.service;

import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.repository.RegistrationKeyRepository;
import de.sigma.sigmabase.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * This service holds all actions for user operations
 * <p/>
 * Created by:  nilsraabe
 * Date:        08.06.15
 * Time:        20:54
 * E-Mail:      satriani.vay@gmail.com
 */
@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Getting current logged in user of this session
     *
     * @return
     */
    public User getUser() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String) {
            return null;
        }

        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) principal;

        String username = userDetails.getUsername();
        notNull(username, "Cant get name of session user out of spring security context!");

        User user = userRepository.findByUsername(username);

        if (user == null) {
            LOG.error("Cant find user by name: '{}' in the database ! (InMemory user not stored in DB ?!)", username);
            return null;
        }

        return user;
    }

    /**
     * Find user by username.
     *
     * @param username
     * @return Attention, could be null !
     */
    public User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Is the user with this name already registered in the database ?
     *
     * @param username
     * @return Attention, could be null !
     */
    public Boolean existUser(String username) {
        User user = userRepository.findByUsername("username");
        return username != null && user != null;
    }

    /**
     * Is the current user of this session authenticated ?
     *
     * @return
     */
    public boolean isAuthenticated() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (principal instanceof String) == false;
    }

    /**
     * Register the user by storing the user object in the database.
     *
     * @param user
     * @return
     */
    @Transactional
    public User registerUser(User user) {

        if (existUser(user.getUsername())) {
            LOG.warn("User with same Username already exists. username: '{}'", user.getUsername());
            return null;
        }

        //Encrypt the password
        String password = user.getPassword();
        password = new ShaPasswordEncoder(256).encodePassword(password, null);
        user.setPassword(password);

        //Store the user with registration key in the db
        userRepository.save(user);

        LOG.info("Created and stored user with reg key in db. user: '{}'", user);

        return user;
    }

    /**
     * Updates information by an existing user
     *
     * @param editedUser the edited user object which is edited by the user
     * @param oldUser    the old user from the db
     * @return
     */
    public User updateUser(User editedUser, User oldUser) {

        if (editedUser == null) {
            LOG.info("Edited User object is null while updating user information.");
            return null;
        }
        if (oldUser == null) {
            LOG.info("OldUser object is null while updating user information.");
            return null;
        }

        /*
        This turnaround is used to keep the existing id of the edited user
         */

        //Check if we have to setup a new username
        if (StringUtils.equals(editedUser.getUsername(), oldUser.getUsername()) == false) {
            throw new IllegalStateException("Username shouldn't be edited !");
        }

        //Check if we have to setup a new password
        if (StringUtils.equals(editedUser.getPassword(), "") == false) {
            String password = editedUser.getPassword();
            password = new ShaPasswordEncoder(256).encodePassword(password, null);
            oldUser.setPassword(password);
        }

        //Check if we have to setup a new e-mail
        if (StringUtils.equals(editedUser.getEmail(), oldUser.getEmail()) == false) {
            oldUser.setEmail(editedUser.getEmail());
        }

        //Check if we have to setup a new forename
        if (StringUtils.equals(editedUser.getForename(), oldUser.getForename()) == false) {
            oldUser.setForename(editedUser.getForename());
        }

        //Check if we have to setup a new surname
        if (StringUtils.equals(editedUser.getSurname(), oldUser.getSurname()) == false) {
            oldUser.setSurname(editedUser.getSurname());
        }

        //Check if we have to setup a new birthday
        if (editedUser.getBirthday().equals(oldUser.getBirthday()) == false) {
            oldUser.setBirthday(editedUser.getBirthday());
        }

        //Check if we have to setup a new gender
        if (editedUser.getGender().equals(oldUser.getGender()) == false) {
            oldUser.setGender(editedUser.getGender());
        }

        //Check if we have to setup a new user description
        if (StringUtils.equals(editedUser.getDescription(), oldUser.getDescription()) == false) {
            oldUser.setDescription(editedUser.getDescription());
        }

        //Update the user in the db
        userRepository.save(oldUser);

        LOG.info("Updated the user information to : {}", oldUser);

        return oldUser;
    }

    /**
     * Login the given user manually by system.
     *
     * @param user
     * @return
     */
    public boolean login(User user) {

        if (user == null) {
            LOG.info("Couldnt login user manually while user object is null.");
            return false;
        }

        if(user.getUserRole() == null) {
            LOG.info("Couldnt login user - userRole is null! user: {}", user);
            return false;
        }

        if(user.getRegistrationKey() == null) {
            LOG.info("Couldnt login user - registration key is null! user: {}", user);
            return false;
        }

        org.springframework.security.core.userdetails.User authUser = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER"));

        Authentication auth = new UsernamePasswordAuthenticationToken(authUser, authUser.getPassword(), authUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        LOG.info("Following user logged in by system. username: '{}', id: '{}'", user.getUsername(), user.getId());

        return true;
    }

    /**
     * Get all registration keys which have NOT a relation to a user == UNused RegistrationKey
     *
     * @param pageable
     * @return
     */
    public Page<User> getAllUsers(Pageable pageable) {
        LOG.debug("Returned pageable of all users.");
        return userRepository.findAll(pageable);
    }

    /**
     * Get a specific user by id
     *
     * @param id user id
     * @return
     */
    public User getUserByID(long id) {
        User user = userRepository.findOne(id);
        LOG.debug("Get user by id. user: {}", user);
        return user;
    }

    /**
     * Delete a specific user by id
     *
     * @param id user id
     * @return
     */
    public boolean deleteUserById(final long id) {
        userRepository.delete(id);
        LOG.debug("Deleted user with id: {}", id);
        return true;
    }


}
