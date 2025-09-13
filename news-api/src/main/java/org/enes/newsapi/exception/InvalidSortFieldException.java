package org.enes.newsapi.exception;

import java.util.List;

public class InvalidSortFieldException extends NewsApiException {
    public InvalidSortFieldException(String sortBy) {
        super(
            "Invalid sort field: " + sortBy,
            "INVALID_SORT_FIELD",
            List.of("Please verify the sort field",
             "Allowed fields: pubDate, source",
             "Provided field: " + sortBy)
        );
    }
}
