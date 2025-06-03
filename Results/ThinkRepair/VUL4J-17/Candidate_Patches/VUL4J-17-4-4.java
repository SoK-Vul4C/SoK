private static String normalizePath(final String path) {
    if (path == null || path.isEmpty()) {
        return ""; // Return an empty string if input is null or empty
    }

    int n = 0;
    
    // Count the number of leading slashes
    while (n < path.length() && path.charAt(n) == '/') {
        n++;
    }

    // Handle trimming of leading slashes
    if (n > 1) {
        return "/" + path.substring(n);
    } else if (n == 0) {
        return path; // If path does not start with a slash, return the original path
    }

    return path; // Return the path with a single leading slash
}