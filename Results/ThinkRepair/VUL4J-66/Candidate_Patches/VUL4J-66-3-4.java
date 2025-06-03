protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    for (int i = 0; i < str.length(); i++) {
        char ch = str.charAt(i);
        if (ch == '/' || ch == '\\' || ch == '?' || ch == ':' || ch == '<' || ch == '>' ||
                ch == ';' || ch == '(' || ch == ')' || ch == '@' || ch == ',' || 
                ch == '[' || ch == ']' || ch == '=') {
            return false; // Return false if an invalid character is found
        }
    }

    return true; // Only return true after checking all characters
}