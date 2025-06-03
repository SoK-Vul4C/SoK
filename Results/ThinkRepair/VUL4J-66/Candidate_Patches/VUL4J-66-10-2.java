protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    for (int i = 0; i < str.length(); i++) {
        char ch = str.charAt(i);
        if (isSpecialCharacter(ch)) {
            return false;
        }
    }

    return true;
}

private static boolean isSpecialCharacter(char ch) {
    switch (ch) {
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
            return true;
        default:
            return false;
    }
}