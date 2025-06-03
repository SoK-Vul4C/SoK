// Fixed Function
protected static boolean isValid(String str) {
    if (str == null || str.length() == 0) return false;
    for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        if (c == '/' || c == '\\' || c == '?' || c == ':' || c == '<' || c == '>' ||
            c == ';' || c == '(' || c == ')' || c == '@' || c == ',' ||
            c == '[' || c == ']' || c == '=') {
            return false; // Special character found
        }
    }
    return true; // No special characters found
}