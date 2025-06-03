protected static boolean isValid(String str) {
    if (str == null || str.length() == 0) return false;
    
    for (int i = 0; i < str.length(); i++) {
        switch (str.charAt(i)) {
            case '/':
            case '\\':
            case '?':
            case ':':
            case '<':
            case '>':
            case ';':
            case '(':
            case ')':
            case '@':
            case ',':
            case '[':
            case ']':
            case '=':
                return false; // Returning immediately if any prohibited character is found
            default:
                break;
        }
    }
    
    return true; // Return true only if no prohibited character is found
}