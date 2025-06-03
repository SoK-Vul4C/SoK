private static String normalizePath(final String path) {
    if (path == null) {
        return null; // Fix Line: Return null if the input path is null
    }

    int n = 0;
    for (; n < path.length(); n++) {
        if (path.charAt(n) != '/') {
            break;
        }
    }

    // Fix Line: Check if the path contains only slashes
    if (n == path.length()) {
        return "/";
    }

    // Fix Line: Handle the case when n is greater than 1
    if (n > 1) {
        return path.substring(n - 1);
    }

    return path;
}