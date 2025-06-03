protected static boolean isValid(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }

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
                return false; // Return false if a restricted character is found
            default:
                // Do nothing for other characters
                break;
        }
    }

    return true; // Return true if no restricted characters are found in the string
}