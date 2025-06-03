private static String normalizePath(final String path) {
    if (path == null) {
        return null;
    }

    String trimmedPath = path.trim(); // Trim leading and trailing spaces
    
    if (trimmedPath.isEmpty()) {
        return null;
    }

    // Normalize the path by replacing consecutive slashes with a single slash
    String normalizedPath = trimmedPath.replaceAll("//+", "/");
    
    if (normalizedPath.startsWith("//")) {
        normalizedPath = "/" + normalizedPath.substring(2);
    }
    
    return normalizedPath;
}