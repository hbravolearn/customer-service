package com.company.ecommerce.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_EMAIL_INVALID_FORMAT_KEY;
import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_EMAIL_REQUIRED_KEY;
import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_FIRSTNAME_REQUIRED_KEY;
import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_LASTNAME_REQUIRED_KEY;
import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_PHONE_NUMBER_INVALID_FORMAT_KEY;
import static com.company.ecommerce.customer.constant.CommonConstant.CUSTOMER_PHONE_NUMBER_REQUIRED_KEY;

@Schema(name = "CustomerRequest", description = "Schema to request customer information")
public record CustomerRequest(

        @Schema(name = "firstName", description = "Customer first name", example = "John")
        @NotEmpty(message = CUSTOMER_FIRSTNAME_REQUIRED_KEY)
        String firstName,

        @Schema(name = "lastName", description = "Customer last name", example = "Doe")
        @NotEmpty(message = CUSTOMER_LASTNAME_REQUIRED_KEY)
        String lastName,

        @Schema(name = "email", description = "Customer email", example = "jdoe@gmail.com")
        @NotEmpty(message = CUSTOMER_EMAIL_REQUIRED_KEY)
        @Email(message = CUSTOMER_EMAIL_INVALID_FORMAT_KEY)
        String email,

        @Schema(name = "phoneNumber", description = "Customer phone number", example = "+51 962329330")
        @NotEmpty(message = CUSTOMER_PHONE_NUMBER_REQUIRED_KEY)
        @Pattern(regexp="^\\+\\d{1,3}\\s?\\d{1,15}$", message = CUSTOMER_PHONE_NUMBER_INVALID_FORMAT_KEY)
        String phoneNumber,

        @Valid
        AddressRequest address

) {

}
