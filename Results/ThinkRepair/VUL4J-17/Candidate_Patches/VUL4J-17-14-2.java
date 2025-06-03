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
    
    if (n > 0) {
        s = s.substring(Math.max(0, n - 1)); // Ensure at least one leading slash
    }

    return s;
}