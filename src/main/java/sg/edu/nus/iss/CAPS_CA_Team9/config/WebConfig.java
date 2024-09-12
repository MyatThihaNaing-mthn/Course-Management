package sg.edu.nus.iss.CAPS_CA_Team9.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Adjust the path pattern to match your API endpoints
            .allowedOrigins("http://localhost:3000") // Adjust the origin to match your React application's URL
            .allowedMethods("GET", "POST", "PUT", "DELETE"); // Adjust the allowed HTTP methods as needed
    }
}
