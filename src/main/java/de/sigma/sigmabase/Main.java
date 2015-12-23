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

    public static void main(String[] args) {
        DateTimeZone.setDefault(DateTimeZone.UTC);
        SpringApplication.run(Main.class, args);
    }
}
