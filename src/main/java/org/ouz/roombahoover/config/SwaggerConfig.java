package org.ouz.roombahoover.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("This is basic simulation endpoints for Roomba Hoover cleaner.");

        Contact myContact = new Contact();
        myContact.setName("Oguz Han Turan");
        myContact.setEmail("turan.oguzhan@hotmail.com");

        Info information = new Info()
                .title("Roomba Hoover Cleaner API")
                .version("1.0")
                .description("This API exposes endpoints to count cleaned patches and the last position of the hoover ")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }


}