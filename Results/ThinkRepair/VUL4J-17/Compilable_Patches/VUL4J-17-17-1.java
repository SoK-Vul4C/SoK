private static String normalizePath(final String path) {
    if (path == null) {
        return ""; // Fix: return an empty string if the input is null
    }

    int n = 0;
    for (; n < path.length(); n++) {
        if (path.charAt(n) != '/') {
            break;
        }
    }

    return path.substring(n); // Fix: remove all leading slashes correctly
}