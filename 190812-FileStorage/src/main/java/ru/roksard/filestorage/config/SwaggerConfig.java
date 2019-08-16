package ru.roksard.filestorage.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("ru.roksard.filestorage"))              
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(apiInfo());                                           
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
          "Сервис загрузки и хранения файлов", 
          "Позволяет хранить файлы в памяти и выполнять базовые операции с ними "
          + "(создание, чтение, обновление, удаление).", 
          "API TOS", 
          "Terms of service", 
          new Contact("Dmitry Ruf", "-", "roksard@gmail.com"), 
          "MIT", "-", new ArrayList<>());
    }
}
