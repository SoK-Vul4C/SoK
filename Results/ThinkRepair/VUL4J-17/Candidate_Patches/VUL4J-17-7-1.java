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
    if (n > 1) {
        return path.substring(n - 1);
    }
    return path;
}