protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false; // Return false for empty or null strings
    }

    for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        if (isSpecialCharacter(c)) {
            return false; // Return false if a special character is found
        }
    }

    return true; // Return true if no special characters were found
}

private static boolean isSpecialCharacter(char c) {
    switch (c) {
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
            return true; // Return true if c is a special character
        default:
            return false; // Return false for non-special characters
    }
}