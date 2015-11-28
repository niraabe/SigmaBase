package de.sigma.sigmabase;

import org.joda.time.DateTimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main-Access point for Raven-Chirp
 *
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
