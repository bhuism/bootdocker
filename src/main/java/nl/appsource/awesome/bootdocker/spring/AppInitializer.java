package nl.appsource.awesome.bootdocker.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.http.WicketServlet;
import org.apache.wicket.spring.SpringWebApplicationFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;

/**
 * 
 * AppInitializer
 *
 */

public class AppInitializer implements ServletContextInitializer {

    static {
        System.setProperty("org.jboss.logging.provider", "slf4j");
        System.setProperty("org.freemarker.loggerLibrary", "SLF4J");
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        addWicketServlet(servletContext);
    }

    protected void addWicketServlet(final ServletContext servletContext) {
        ServletRegistration.Dynamic wicketFilterReg = servletContext.addServlet("wicketServlet", WicketServlet.class);
        wicketFilterReg.setLoadOnStartup(1);
        wicketFilterReg.addMapping("/*");
        wicketFilterReg.setInitParameter(WicketFilter.APP_FACT_PARAM, SpringWebApplicationFactory.class.getCanonicalName());
        wicketFilterReg.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
    }

}
