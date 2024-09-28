package com.company.ecommerce.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AddressResponse", description = "Scheme to respond customer information")
public record AddressResponse(

        @Schema(name = "street", description = "Customer street", example = "218 Newbury Street")
        String street,

        @Schema(name = "city", description = "Customer city", example = "Chicago")
        String city,

        @Schema(name = "state", description = "Customer state", example = "Illinois")
        String state,

        @Schema(name = "zipCode", description = "Customer zip code", example = "15088")
        String zipCode,

        @Schema(name = "country", description = "Customer country", example = "USA")
        String country

) {
}
