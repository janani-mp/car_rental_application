//http://localhost:9192/swagger-ui/index.html#/


package com.carRental.car_rental_app.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Value("${car-rental.openapi.dev-url}")
    private String devUrl;

    @Value("${car-rental.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI carRentalOpenAPI() {
        Server devServer = new Server()
            .url(devUrl)
            .description("Development Environment");

        Server prodServer = new Server()
            .url(prodUrl)
            .description("Production Environment");

        Contact contact = new Contact()
            .name("Car Rental API Support")
            .email("support@carrental.com")
            .url("https://carrental.com/contact");

        Info info = new Info()
            .title("Car Rental Management API")
            .version("1.0.0")
            .contact(contact)
            .description("API for managing car rental operations")
            .license(new License().name("Apache 2.0").url("https://springdoc.org"));

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer, prodServer));
    }
}