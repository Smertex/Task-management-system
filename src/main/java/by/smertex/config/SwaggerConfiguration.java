package by.smertex.config;

import by.smertex.util.ApiPath;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi publicTaskApi(){
        return GroupedOpenApi.builder()
                .group("Tasks")
                .pathsToMatch(ApiPath.TASK_PATH + ApiPath.ALL_PATH)
                .build();
    }

    @Bean
    public GroupedOpenApi publicAuthApi(){
        return GroupedOpenApi.builder()
                .group("Authentication")
                .pathsToMatch(ApiPath.AUTH_PATH)
                .build();
    }

    @Bean
    public OpenAPI customOpenApi(
            @Value("${application-data.description}") String description,
            @Value("${application-data.version}") String version,
            @Value("${application-data.name}") String appName,
            @Value("${application-data.contact.vk}") String contactVk) {

        return new OpenAPI().info(new Info()
                .title(appName)
                .version(version)
                .description(description)
                .contact(new Contact()
                        .name("Контактная информация ")
                        .url(contactVk)));
    }

}
