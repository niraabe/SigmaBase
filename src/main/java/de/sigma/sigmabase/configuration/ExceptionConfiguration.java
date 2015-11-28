package de.sigma.sigmabase.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * "Controller" for global exception handling
 * <p/>
 * Created by:  nilsraabe
 * Date:        02.07.15
 * Time:        11:00
 * E-Mail:      satriani.vay@gmail.com
 */
@ControllerAdvice
public class ExceptionConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalError(Exception e, HttpServletRequest request) {

        LOG.error("Request GET to '/error' - some error occurred !");
        LOG.error(request.toString());
        LOG.error(e.toString());
        LOG.error(e.getStackTrace().toString());

        ModelAndView error = new ModelAndView("error");
        return error;
    }

}
