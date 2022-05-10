package umg.foka.penkeats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import umg.foka.penkeats.filters.AuthFilter;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan("umg.foka.penkeats.controllers")
@ComponentScan("umg.foka.penkeats.repositories")
@ComponentScan("umg.foka.penkeats.services")
@ComponentScan("umg.foka.penkeats.filters")
@ComponentScan("umg.foka.penkeats.exceptions")
public class PenkEatsApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(PenkEatsApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        AuthFilter authFilter = new AuthFilter();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "*"
                )
                .allowedMethods(
                        "GET",
                        "PUT",
                        "POST",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                );
    }
}
