package org.enes.newsapi.exception;

import java.util.List;
import java.util.Set;

public class InvalidRequestParamException extends NewsApiException {
    public InvalidRequestParamException(Set<String> invalidParams) {
        super("Invalid request parameters: " + invalidParams,
        "INVALID_REQUEST_PARAMETERS",
        List.of("Please verify the request parameters", 
                "Invalid parameters: " + invalidParams,
                "Check the API documentation for allowed parameters")
        );
    }
}
