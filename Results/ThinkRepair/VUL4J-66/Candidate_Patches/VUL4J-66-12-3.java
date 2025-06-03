// Fixed Function
protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false; // Empty or null string is not valid
    }

    for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        if (c == '/' || c == '\\' || c == '?' || c == ':' || c == '<' || c == '>' ||
            c == ';' || c == '(' || c == ')' || c == '@' || c == ',' ||
            c == '[' || c == ']' || c == '=') {
            return false; // Special character found, return false immediately
        }
    }
    
    return true; // No special characters found, return true
}