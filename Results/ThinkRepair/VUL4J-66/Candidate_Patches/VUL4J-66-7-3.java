protected static boolean isValid(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }

    boolean isValid = true; // Assume string is valid initially

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
                isValid = false; // Set flag to false if any invalid character found
                break; // Exit the switch statement
        }
    }

    return isValid; // Return the final validity flag after checking all characters
}