package de.sigma.sigmabase.configuration;

import de.sigma.sigmabase.controller.util.RegistrationKeyGenerator;
import de.sigma.sigmabase.model.user.Gender;
import de.sigma.sigmabase.model.user.RegistrationKey;
import de.sigma.sigmabase.model.user.User;
import de.sigma.sigmabase.model.user.UserRole;
import de.sigma.sigmabase.service.UserService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.sql.DataSource;

/**
 * Configuration file to handle login and logout requests
 *
 * Created by:  nilsraabe
 * Date:        16.06.15
 * Time:        15:42
 * E-Mail:      satriani.vay@gmail.com
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment environment;


    @Autowired
    private  RegistrationKeyGenerator registrationKeyGenerator;

    @Autowired
    private UserService userService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (environment.acceptsProfiles("heroku") == false) {
            LOG.warn("Attention - CSRF disabled !");
            http.csrf().disable();
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        }


        http.authorizeRequests()
                .antMatchers("/**").permitAll()

                .and()
                .formLogin()
                .loginPage("/")
                .failureUrl("/login")

                .and()
                .logout()
                .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        if (environment.acceptsProfiles("heroku") == false) {
            LOG.warn("Adding default user. name: user, password: foo, role: ROLE_USER");
            auth
                .inMemoryAuthentication()
                .withUser("user")
                .password("foo")
                    .roles("USER");
            //TODO defaulöt user in DB
            createData();
        }

        auth
                .jdbcAuthentication()
                .passwordEncoder(new ShaPasswordEncoder(256))
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, 'true' from t_user where username = ?")
                .authoritiesByUsernameQuery("select username, 'ROLE_USER' from t_user where username = ?");

    }

    private void createData() {

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
