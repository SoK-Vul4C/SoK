private static String normalizePath(final String path) {
    if (path == null || path.isEmpty()) {
        return path;
    }

    int n = 0;
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    // Removing the leading slashes
    if (n > 1) {
        return path.substring(n - 1);
    }
    
    return path;
}