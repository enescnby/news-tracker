package org.enes.newsapi.exception;

import java.util.List;

public class InvalidSortDirectionException extends NewsApiException {
    public InvalidSortDirectionException(String sortDir) {
        super(
            "Invalid sort direction: " + sortDir,
            "INVALID_SORT_DIRECTION",
            List.of(
                "Please verify the sort direction",
                "Allowed directions: asc, desc",
                "Provided direction: " + sortDir
            )
        );
    }
}
