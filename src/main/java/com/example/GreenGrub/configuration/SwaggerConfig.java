package com.example.GreenGrub.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info().title("Green Grub").description("This is a platform where we distribute surplus food from multiple org./restaurants")
                )
                .servers(Arrays.asList(
                new Server().url("http://localhost:8081").description("localhost"),
                new Server().url("http://localhost:8809").description("live") // change this with the live url
                ))
//                .tags(Arrays.asList(new Tag().name(""))) //tags to change the order of controller being render in UI
                //uncomment the below part when JWT implementation is done
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
//                .components(new Components().addSecuritySchemes(
//                        "bearerAuth",new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")
//                                .in(SecurityScheme.In.HEADER)
//                                .name("Authorization")
//                ))
                ;

    }
}
