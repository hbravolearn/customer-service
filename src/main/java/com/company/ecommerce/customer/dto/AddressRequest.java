package com.company.ecommerce.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_CITY_REQUIRED_KEY;
import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_COUNTRY_REQUIRED_KEY;
import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_STATE_REQUIRED_KEY;
import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_STREET_REQUIRED_KEY;
import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_ZIP_CODE_REQUIRED_KEY;

@Schema(name = "AddressRequest", description = "Schema to request address information")
@Validated
public record AddressRequest(

        @Schema(name = "street", description = "Customer street", example = "218 Newbury Street")
        @NotEmpty(message = CUSTOMER_STREET_REQUIRED_KEY)
        String street,

        @Schema(name = "city", description = "Customer city", example = "Chicago")
        @NotEmpty(message = CUSTOMER_CITY_REQUIRED_KEY)
        String city,

        @Schema(name = "state", description = "Customer state", example = "Illinois")
        @NotEmpty(message = CUSTOMER_STATE_REQUIRED_KEY)
        String state,

        @Schema(name = "zipCode", description = "Customer zip code", example = "15088")
        @NotEmpty(message = CUSTOMER_ZIP_CODE_REQUIRED_KEY)
        String zipCode,

        @Schema(name = "country", description = "Customer country", example = "USA")
        @NotEmpty(message = CUSTOMER_COUNTRY_REQUIRED_KEY)
        String country

) {
}
