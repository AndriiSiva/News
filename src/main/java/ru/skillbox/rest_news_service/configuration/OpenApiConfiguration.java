package ru.skillbox.rest_news_service.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Value("${server.url}")
    private String serverUrl;
    @Bean
    public OpenAPI openAPIDescription() {
        Server localhostServer = new Server();
        localhostServer.setUrl(serverUrl);
        localhostServer.setDescription("Local env");
        Server productionServer = new Server();
        productionServer.setUrl("http://some.prod.url");
        productionServer.setDescription("Production env");

        Contact contact = new Contact();
        contact.setName("Pavel Bakanov");
        contact.setEmail("pashtet@mail.ru");
        contact.setUrl("http://some.url");
        License mitLicense = new License().name("GNU AGPLv3").url("https://chooselicense.com/lisenses/agpl-3.0/");
        Info info = new Info().title("NEWS API").version("1.0").contact(contact).description("API for news").termsOfService("https://some.terms.url").license(mitLicense);
        return new OpenAPI().info(info).servers(List.of(localhostServer, productionServer));

    }
}