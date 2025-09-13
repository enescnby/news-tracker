package org.enes.newsapi.exception;

import java.util.Set;

public class InvalidRequestParamException extends RuntimeException {
    public InvalidRequestParamException(Set<String> invalidParams) {
        super("Invalid request parameters: " + invalidParams);
    }
}
