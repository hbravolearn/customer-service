package com.company.ecommerce.customer.api;

import com.company.ecommerce.customer.dto.CustomerRequest;
import com.company.ecommerce.customer.dto.CustomerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

public interface CustomerApi {

    @GetMapping
    @Operation(
            summary = "Find all customers",
            description = "REST API to find all customers",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            in = HEADER,
                            description = "Language",
                            example = "en",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "X-Api-Version",
                            in = HEADER,
                            description = "Api version",
                            example = "v1",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Http status OK",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = CustomerResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Http status INTERNAL SERVER ERROR",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    ResponseEntity<List<CustomerResponse>> findAllCustomers();

    @GetMapping("/{id}")
    @Operation(
            summary = "Find customer by id",
            description = "REST API to find customer by id",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            in = HEADER,
                            description = "Language",
                            example = "en",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "X-Api-Version",
                            in = HEADER,
                            description = "Api version",
                            example = "v1",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "id",
                            in = PATH,
                            description = "Customer id",
                            example = "34ffabc840271a2600f50d326",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Http status OK",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomerResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Http status NOT FOUND",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Http status INTERNAL SERVER ERROR",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    ResponseEntity<CustomerResponse> findCustomerById(@PathVariable String id);

    @PostMapping
    @Operation(
            summary = "Create customer",
            description = "REST API to create customer",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            in = HEADER,
                            description = "Language",
                            example = "en",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "X-Api-Version",
                            in = HEADER,
                            description = "Api version",
                            example = "v1",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer object to create",
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomerRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Http status CREATED",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomerResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Http status BAD REQUEST",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Http status INTERNAL SERVER ERROR",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest);

    @PutMapping("/{id}")
    @Operation(
            summary = "Update customer by id",
            description = "REST API to update customer by id",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            in = HEADER,
                            description = "Language",
                            example = "en",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "X-Api-Version",
                            in = HEADER,
                            description = "Api version",
                            example = "v1",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "id",
                            in = PATH,
                            description = "Customer id",
                            example = "34ffabc840271a2600f50d326",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer object to update",
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CustomerRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Http status OK",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CustomerResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Http status BAD REQUEST",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Http status NOT FOUND",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Http status INTERNAL SERVER ERROR",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    ResponseEntity<CustomerResponse> updateCustomerById(
            @PathVariable String id, @Valid @RequestBody CustomerRequest customerRequest);

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete customer by id",
            description = "REST API to delete customer by id",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            in = HEADER,
                            description = "Language",
                            example = "en",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "X-Api-Version",
                            in = HEADER,
                            description = "Api version",
                            example = "v1",
                            required = true,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "id",
                            in = PATH,
                            description = "Customer id",
                            example = "34ffabc840271a2600f50d326",
                            required = true,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Http status NO CONTENT"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Http status NOT FOUND",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Http status INTERNAL SERVER ERROR",
                            content = @Content(
                                    mediaType = APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    ResponseEntity<Void> deleteCustomerById(@PathVariable String id);

}
