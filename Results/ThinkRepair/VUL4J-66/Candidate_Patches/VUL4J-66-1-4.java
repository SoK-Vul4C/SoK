protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    boolean isValid = true;
    
    for (int i = 0; i < str.length(); i++) {
        char currentChar = str.charAt(i);
        if (currentChar == '/' || currentChar == '\\' || currentChar == '?' || currentChar == ':' ||
            currentChar == '<' || currentChar == '>' || currentChar == ';' || currentChar == '(' ||
            currentChar == ')' || currentChar == '@' || currentChar == ',' || currentChar == '[' ||
            currentChar == ']' || currentChar == '=') {
            isValid = false;
            break; // Exit loop if invalid character is found
        }
    }

    return isValid;
}