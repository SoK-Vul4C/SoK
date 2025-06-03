private static String normalizePath(final String path) {
    if (path == null) {
        return ""; // Fix: Return empty string for null input
    }

    int n = 0;
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    return path.substring(n); // Fix: Removing the leading slashes correctly
}