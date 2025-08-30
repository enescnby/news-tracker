package org.enes.newsapi.config;

import org.enes.newsapi.interceptor.RequestParamValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestParamValidationInterceptor requestParamValidationInterceptor;

    public WebConfig(RequestParamValidationInterceptor requestParamValidationInterceptor) {
        this.requestParamValidationInterceptor = requestParamValidationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestParamValidationInterceptor);
    }
}
