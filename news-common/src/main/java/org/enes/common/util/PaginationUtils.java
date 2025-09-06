package org.enes.common.util;

public class PaginationUtils {
    public static boolean isAscending(String sortDir) {
        if (sortDir == null) { throw new IllegalArgumentException("sortDir cannot be null"); }
        return sortDir.equalsIgnoreCase("asc");
    }
}
