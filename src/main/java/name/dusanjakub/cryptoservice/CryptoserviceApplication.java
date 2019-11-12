package name.dusanjakub.cryptoservice;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@SpringBootApplication
public class CryptoserviceApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(CryptoserviceApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("/webjars/");
    }

    @Configuration
    public static class MvcConfig extends WebMvcConfigurationSupport {
        @Override
        @Bean
        public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
            RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
            HandlerMethodArgumentResolver resolver = new LazyBodyArgumentResolver(adapter);
            adapter.setCustomArgumentResolvers(Collections.singletonList(resolver));
            return adapter;
        }
    }
}
