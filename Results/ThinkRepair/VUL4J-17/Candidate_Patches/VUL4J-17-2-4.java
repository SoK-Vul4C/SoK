private static String normalizePath(final String path) {
    if (path == null) {
        return null;
    }

    int n = 0;
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    if (n == 0) {
        return path;
    } else if (n == 1) {
        return "/" + path.substring(n);
    } else {
        return path.substring(n - 1);
    }
}