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
                // Set flag if any prohibited character is found
                containsProhibitedChar = true;
                break; // Exit the switch statement after setting the flag
            default:
                // No prohibited character found, continue checking other characters
                break;
        }
        
        if(containsProhibitedChar) {
            // Exit the loop early if any prohibited character is found
            break;
        }
    }
    
    return !containsProhibitedChar; // Return true only if no prohibited character is found
}