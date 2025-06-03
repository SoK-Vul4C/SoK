protected static boolean isValid(String str) {
    if (str == null || str.length() == 0) return false;
    
    boolean isValid = true;
    
    for (int i = 0; i < str.length(); i++) {
        char currentChar = str.charAt(i);
        // Check if the current character is an invalid character
        switch (currentChar) {
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
                isValid = false;
                break;
            default:
                // Do nothing for valid characters
                break;
        }
    }
    
    return isValid;
}