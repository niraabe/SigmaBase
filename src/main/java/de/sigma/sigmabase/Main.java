package de.sigma.sigmabase;

import de.sigma.sigmabase.controller.util.RegistrationKeyGenerator;
import de.sigma.sigmabase.model.user.Gender;
import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.model.user.UserRole;
import de.sigma.sigmabase.service.UserService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main-Access point for Raven-Chirp
 * <p/>
 * Created by:  nilsraabe
 * Date:        07.06.15
 * Time:        19:54
 * E-Mail:      satriani.vay@gmail.com
 */
@SpringBootApplication
public class Main {

    @Autowired
    private  static RegistrationKeyGenerator registrationKeyGenerator;

    @Autowired
    private static UserService userService;

    public static void main(String[] args) {
        DateTimeZone.setDefault(DateTimeZone.UTC);

        //TODO nils remove this !
        createData();

        SpringApplication.run(Main.class, args);
    }

    private static void createData() {

        String text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et " +
                "accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing " +
                "elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd " +
                "gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";


        RegistrationKey registrationKey = new RegistrationKey();
        registrationKey.setKey(registrationKeyGenerator.generateRandomKey());
        registrationKey.setUserRole(UserRole.ADMIN);

        User user = new User();
        user.setBirthday(new DateTime());
        user.setEmail("andreas@raabe.de");
        user.setPassword("andreas1234");
        user.setDescription(text);
        user.setForename("Andreas");
        user.setSurname("Raabe");
        user.setGender(Gender.MALE);
        user.setUsername("Andreas");
        user.setRegistrationKey(registrationKey);

        userService.registerUser(user);

    }

}
