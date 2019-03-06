package nl.appsource.awesome.bootdocker;

import javax.inject.Inject;

import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.resource.JQueryResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.crypt.CharEncoding;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.awesome.bootdocker.page.Home;

@Slf4j
public class WicketApplication extends WebApplication {

    @Inject
    private ApplicationContext applicationContext;

    public WicketApplication() {
        setConfigurationType(RuntimeConfigurationType.DEPLOYMENT);
    }

    @Override
    public void init() {
        super.init();
        getRequestCycleSettings().setResponseRequestEncoding(CharEncoding.UTF_8);
        getMarkupSettings().setDefaultMarkupEncoding(CharEncoding.UTF_8);
        getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));
        getJavaScriptLibrarySettings().setJQueryReference(JQueryResourceReference.getV3());
    }
    
    @Override
    public Class<? extends Page> getHomePage() {
        return Home.class;
    }
   

}
