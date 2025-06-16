package com.awstraining.backend.common.exception;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import com.awstraining.backend.api.rest.v1.model.ApiBusinessErrorResponse;
import com.awstraining.backend.domain.measurement.CouldNotSaveMeasurementException;
import com.awstraining.backend.domain.measurement.UnknownDeviceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UnknownDeviceException.class)
    public ResponseEntity<ApiBusinessErrorResponse> toResponse(final HttpServletRequest request,
                                                               final UnknownDeviceException unknownDeviceException) {
        return unknownDeviceException.toResponse(request);
    }

    @ExceptionHandler(CouldNotSaveMeasurementException.class)
    public ResponseEntity<ApiBusinessErrorResponse> toResponse(final HttpServletRequest request,
            final CouldNotSaveMeasurementException couldNotSaveMeasurementException) {
        return couldNotSaveMeasurementException.toResponse(request);
    }
}

