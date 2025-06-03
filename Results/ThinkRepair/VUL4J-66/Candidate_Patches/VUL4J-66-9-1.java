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
                // This is moved outside the switch statement to check all characters
                break;
            default:
                break;
        }
    }
    // Return false after checking all characters
    return true;
}