package sg.edu.nus.iss.CAPS_CA_Team9.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: config
 * @description: 配置类
 * @author: shenning
 * @create: 2023-06-16 22:15
 **/
@Configuration
public class Myconfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("../resources/templates/", ".html"); // 设置JSP视图解析器
    }
    @Override
    public void addCorsMappings(CorsRegistry registry){
        //设置允许跨域的路径
        registry.addMapping ("/**")
                //设置允许跨域请求的域名
                .allowedOriginPatterns ("*")
                //是否允许证书
                .allowCredentials (true)
                //设置允许的方法
                .allowedMethods ("GET","POST")
                //设置允许的header属性
                .allowedHeaders ("*")
                //允许跨域时间
                .maxAge (3600);
    }


}
