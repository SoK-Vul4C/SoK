private static String normalizePath(final String path) {
    if (path == null) {
        return null;
    }

    int n = 0;
    for (; n < path.length(); n++) {
        if (path.charAt(n) != '/') {
            break;
        }
    }

    return n > 1 ? path.substring(n - 1) : path;
}