package pl.desertcacti.mtgcardsshopsystem.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/** CorsConfig class creates and configures a Cross Origin Resource Sharing filter. */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /** corsFilter()
     /*  Method road map:
     *1* Create URL-based configuration source.
     *2* Configure settings: allow credentials, specify allowed origin, headers, and HTTP methods.
     *3* Register configuration for all endpoints. */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
