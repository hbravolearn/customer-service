package com.company.ecommerce.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CustomerResponse", description = "Scheme to respond customer information")
public record CustomerResponse(

        @Schema(name = "customerId", description = "Customer id", example = "66a680bb38fd8f5c93f3d220")
        String customerId,

        @Schema(name = "firstName", description = "Customer first name", example = "John")
        String firstName,

        @Schema(name = "lastName", description = "Customer last name", example = "Doe")
        String lastName,

        @Schema(name = "email", description = "Customer email", example = "jdoe@gmail.com")
        String email,

        @Schema(name = "phoneNumber", description = "Customer phone number", example = "+51 962329330")
        String phoneNumber,

        AddressResponse address

) {
}
