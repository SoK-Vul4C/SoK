protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    boolean isValid = true; // Assume string is valid initially

    for (int i = 0; i < str.length(); i++) {
        char currentChar = str.charAt(i);
        if (currentChar == '/' || currentChar == '\\' || currentChar == '?' ||
            currentChar == ':' || currentChar == '<' || currentChar == '>' ||
            currentChar == ';' || currentChar == '(' || currentChar == ')' ||
            currentChar == '@' || currentChar == ',' || currentChar == '[' ||
            currentChar == ']' || currentChar == '=') {
            isValid = false;
            // No need to return here, continue checking other characters
        }
    }

    return isValid; // Return the final validity flag after checking all characters
}