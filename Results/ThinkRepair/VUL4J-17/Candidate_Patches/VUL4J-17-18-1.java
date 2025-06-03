private static String normalizePath(final String path) {
    if (path == null) {
        return ""; // Fix: Return empty string for null input
    }

    int n = 0;
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    if (n > 1) {
        return path.substring(n - 1); // Fix: Adjust substring start index
    }

    return path;
}