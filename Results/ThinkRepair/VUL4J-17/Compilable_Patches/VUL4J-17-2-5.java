private static String normalizePath(final String path) {
    if (path == null) {
        return null;
    }

    StringBuilder normalizedPath = new StringBuilder();
    boolean slashAppended = false;

    for (int i = 0; i < path.length(); i++) {
        char c = path.charAt(i);

        if (c == '/') {
            if (!slashAppended) {
                normalizedPath.append(c);
                slashAppended = true;
            }
        } else {
            normalizedPath.append(c);
            slashAppended = false;
        }
    }

    return normalizedPath.toString();
}