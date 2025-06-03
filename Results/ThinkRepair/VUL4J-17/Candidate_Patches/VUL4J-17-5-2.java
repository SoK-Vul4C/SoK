private static String normalizePath(final String path) {
    // Check if the input path is null or empty
    if (path == null || path.isEmpty()) {
        return "/";
    }

    int n = 0;
    // Skip leading slashes
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    if (n >= path.length()) {
        return "/";
    }

    return path.substring(n);
}