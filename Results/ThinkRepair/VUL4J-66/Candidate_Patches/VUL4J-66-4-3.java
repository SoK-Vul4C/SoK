protected static boolean isValid(String str) {
    if (str == null || str.length() == 0) {
        return false;
    }

    for (int i = 0; i < str.length(); i++) {
        char currentChar = str.charAt(i);
        if (isRestrictedCharacter(currentChar)) {
            return false;
        }
    }

    return true;
}

private static boolean isRestrictedCharacter(char c) {
    return c == '/' || c == '\\' || c == '?' || c == ':' ||
           c == '<' || c == '>' || c == ';' || c == '(' ||
           c == ')' || c == '@' || c == ',' || c == '[' ||
           c == ']' || c == '=';
}