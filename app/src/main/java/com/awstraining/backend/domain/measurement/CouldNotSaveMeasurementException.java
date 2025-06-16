package com.awstraining.backend.domain.measurement;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.status;

import com.awstraining.backend.api.rest.v1.model.ApiBusinessErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public class CouldNotSaveMeasurementException extends RuntimeException {
    public ResponseEntity<ApiBusinessErrorResponse> toResponse(final HttpServletRequest request) {
        final int status = INTERNAL_SERVER_ERROR.value();
        final ApiBusinessErrorResponse apiClientErrorResponse = new ApiBusinessErrorResponse() //
                .statusCode(status) //
                .logMessage("Given measurement could not be saved in the database due to the internal server error.") //
                .requestUrl(request.getRequestURL().toString()) //
                .requestTimestamp(currentTimeMillis());
        return status(status).body(apiClientErrorResponse);
    }
}
