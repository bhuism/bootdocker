package nl.appsource.awesome.bootdocker;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.awesome.bootdocker.config.HalFormsApplication;
import nl.appsource.awesome.bootdocker.rest.WebFluxCitaatController;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackageClasses = { WebFluxCitaatController.class, HalFormsApplication.class })
public class DockerSpringBootApplication {

    public static void main(String[] args) {
        log.info("Main start");
        SpringApplication.run(DockerSpringBootApplication.class, args);
    }

    @Bean
    public WebApplication webApplication() {
        return new WicketApplication();
    }

}
