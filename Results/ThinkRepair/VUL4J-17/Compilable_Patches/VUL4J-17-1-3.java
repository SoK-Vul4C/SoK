private static String normalizePath(final String path) {
    if (path == null || path.isEmpty()) {
        return path;
    }

    int n = 0;
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    if (n > 1) {
        // Find the first non-slash character after consecutive slashes
        while (n < path.length() && path.charAt(n) == '/') {
            n++;
        }
        return path.substring(n - 1);
    }
    return path;
}