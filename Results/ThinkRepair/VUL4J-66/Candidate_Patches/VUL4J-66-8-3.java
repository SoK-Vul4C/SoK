protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    boolean containsSpecialCharacter = false;
    for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
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
                containsSpecialCharacter = true;
                break; // break the switch statement when a special character is found
            default:
                // Do nothing, continue checking other characters
        }
    }

    return !containsSpecialCharacter; // return true only if no special character exists
}