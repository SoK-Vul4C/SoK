protected static boolean isValid(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }
    
    boolean containsProhibitedChar = false;
    
    for (int i = 0; i < str.length(); i++) {
        char currentChar = str.charAt(i);
        
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
                containsProhibitedChar = true;
                break;
            default:
                // No prohibited character found, continue checking
                break;
        }
    }
    
    return !containsProhibitedChar; // Return true only if no prohibited character is found
}