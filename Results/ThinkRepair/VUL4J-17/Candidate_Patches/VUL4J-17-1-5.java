private static String normalizePath(final String path) {
    if (path == null || path.isEmpty()) {
        return path;
    }

    int n = 0;
    // Check for consecutive leading slashes
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    // Check if the path consists only of slashes
    if (n == path.length()) {
        return "/";
    }

    // Removing the leading slashes
    if (n > 1) {
        return path.substring(n - 1);
    }

    return path;
}