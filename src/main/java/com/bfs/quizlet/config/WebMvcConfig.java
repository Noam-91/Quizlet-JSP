package com.bfs.quizlet.config;

import com.bfs.quizlet.filter.LoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private LoginFilter loginFilter;

    @Autowired
    public WebMvcConfig(LoginFilter loginFilter) {
        this.loginFilter = loginFilter;
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }



//    @Bean
//    public FilterRegistrationBean<LoginFilter> filterRegistrationBean() {
//        FilterRegistrationBean<LoginFilter> registrationBean = new FilterRegistrationBean<>();
//        LoginFilter loginFilter = new LoginFilter();
//
//        registrationBean.setFilter(loginFilter);
//        registrationBean.addUrlPatterns("/login/*");
//        registrationBean.setOrder(2); //set precedence
//        return registrationBean;
//    }
}
