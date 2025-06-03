private static String normalizePath(final String path) {
    String s = path;
    if (s == null) {
        return null;
    }
    
    int n = 0;
    for (; n < s.length(); n++) {
        if (s.charAt(n) != '/') {
            break;
        }
    }

    // Trim all leading slashes
    if (n > 0) {
        s = s.substring(n);
    }

    return s;
}