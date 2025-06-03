private static String normalizePath(final String path) {
    if (path == null || path.isEmpty()) {
        return "";
    }

    int n = 0;
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    return path.substring(n);
}