package org.enes.newsapi.exception;

import java.util.List;

public class InvalidPageSizeException extends NewsApiException {
    public InvalidPageSizeException(int pageSize) {
        super(
            "Invalid page size: " + pageSize,
            "INVALID_PAGE_SIZE",
            List.of(
                "Page size must be between 1 and 100",
                "Provided size: " + pageSize
            )
        );
    }
}
