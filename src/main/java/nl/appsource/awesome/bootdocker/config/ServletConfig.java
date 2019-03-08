package nl.appsource.awesome.bootdocker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ServletConfig {

    @Bean
    public AppInitializer appInitializer() {
        return new AppInitializer();
    }
    
}
