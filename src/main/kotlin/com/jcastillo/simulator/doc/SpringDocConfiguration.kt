package com.jcastillo.simulator.doc

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme

@Configuration
class SpringDocConfiguration {

    @Bean
    fun apiInfo(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Power Plant Simulator - OpenAPI 3.0")
                    .description("API for simulating and managing solar power plants network. Provides functionality to: * Load power plants * Calculate output * Monitor network state")
                    .termsOfService("https://swagger.io/terms/")
                    .contact(
                        Contact()
                            .email("castillo.guerra@gmail.com")
                    )
                    .license(
                        License()
                            .name("Apache 2.0")
                            .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                    )
                    .version("1.0.12")
            )
            .components(
                Components()
                    .addSecuritySchemes("ApiKeyAuth", SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .`in`(SecurityScheme.In.HEADER)
                        .name("X-API-Key")
                    )
            )
    }
}
