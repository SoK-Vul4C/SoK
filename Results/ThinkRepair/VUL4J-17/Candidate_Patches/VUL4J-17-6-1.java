private static String normalizePath(final String path) {
    String s = path;
    
    if (s == null) {
        return "";
    }
    
    int n = 0;
    
    for (; n < s.length(); n++) {
        if (s.charAt(n) != '/') {
            break;
        }
    }
    
    if (n > 0) {
        s = s.substring(n);
    }
    
    return s;
}