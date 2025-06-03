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
                return false; // Return false immediately when a forbidden character is found
            default:
                break;
        }
    }
    // Return true only if none of the forbidden characters were found
    return true;
}