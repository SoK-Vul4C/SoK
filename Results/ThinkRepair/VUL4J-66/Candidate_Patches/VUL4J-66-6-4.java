protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
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
                return false; // Return false immediately if any special character is found
            default:
                break;
        }
    }

    // If no special characters were found, return true
    return true;
}