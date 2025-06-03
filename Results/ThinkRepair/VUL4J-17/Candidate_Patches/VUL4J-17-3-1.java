private static String normalizePath(final String path) {
    if (path == null) {
        return ""; // Return an empty string if input path is null
    }

    int n = 0;
    for (; n < path.length(); n++) {
        if (path.charAt(n) != '/') {
            break;
        }
    }

    return path.substring(n); // Return the substring from index 'n'
}