package de.sigma.sigmabase.configuration;

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
        }

        auth
                .jdbcAuthentication()
                .passwordEncoder(new ShaPasswordEncoder(256))
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, 'true' from t_user where username = ?")
                .authoritiesByUsernameQuery("select username, 'ROLE_USER' from t_user where username = ?");

    }
}
