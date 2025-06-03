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
                return false;
            default:
                break;
        }
    }

    // If none of the special characters were found, return true
    return true;
}