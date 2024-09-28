package com.company.ecommerce.customer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@OpenAPIDefinition(
        info = @Info(
                title = "Customer API Definition",
                description = "Documentation for customer service",
                termsOfService = "http://swagger.io/terms/",
                contact = @Contact(
                        name = "Api Team",
                        email = "apiteam@swagger.io",
                        url = "http://swagger.io"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                ),
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        description = "LOCAL SERVER",
                        url = "http://localhost:8081"
                ),
                @Server(
                        description = "DEV SERVER",
                        url = "http://dev.company.com"
                ),
                @Server(
                        description = "QA SERVER",
                        url = "http://qa.company.com"
                ),
                @Server(
                        description = "PROD SERVER",
                        url = "http://company.com"
                )
        }
)
@SecurityScheme(
        name = "SecurityToken",
        description = "Access token for my API",
        type = HTTP,
        paramName = AUTHORIZATION,
        in = HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
