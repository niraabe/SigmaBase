package de.sigma.sigmabase.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuration file for static access to some sites
 *
 * Created by:  nilsraabe
 * Date:        18.06.15
 * Time:        20:00
 * E-Mail:      satriani.vay@gmail.com
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/search").setViewName("search");
        registry.addViewController("/about").setViewName("about");
    }
}
