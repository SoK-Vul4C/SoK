private static String normalizePath(final String path) {
    if (path == null || path.isEmpty()) {
        return ""; // Return an empty string if input is null or empty
    }

    int n = 0;
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    if (n > 1) {
        return path.substring(n - 1);
    }

    return path;
}