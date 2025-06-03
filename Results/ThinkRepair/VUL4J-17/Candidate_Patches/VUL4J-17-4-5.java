private static String normalizePath(final String path) {
    if (path == null || path.isEmpty()) {
        return ""; // Return an empty string if input is null or empty
    }

    int n = 0;

    // Find the first non-slash character
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    // Remove all but one leading slash
    if (n > 1) {
        return "/" + path.substring(n);
    }

    return path;
}