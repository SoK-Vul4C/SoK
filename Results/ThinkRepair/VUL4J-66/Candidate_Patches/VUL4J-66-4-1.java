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
                return false; // Return false only if a restricted character is found
            default:
                break;
        }
    }
    
    return true; // Return true if no restricted characters are found in the string
}