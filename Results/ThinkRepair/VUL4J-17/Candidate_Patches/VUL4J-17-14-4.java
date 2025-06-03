private static String normalizePath(final String path) {
    if (path == null) {
        return null;
    }

    String s = path;
    int n = 0;
    for (; n < s.length(); n++) {
        if (s.charAt(n) != '/') {
            break;
        }
    }

    if (n > 1) {
        // Replace consecutive slashes with a single slash
        s = "/" + s.substring(n - 1).replaceAll("/+", "/");
    }

    return s;
}