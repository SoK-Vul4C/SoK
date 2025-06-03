protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    for (int i = 0; i < str.length(); i++) {
        char currentChar = str.charAt(i);
        if (currentChar == '/' || 
            currentChar == '\\' || 
            currentChar == '?' || 
            currentChar == ':' || 
            currentChar == '<' || 
            currentChar == '>' || 
            currentChar == ';' || 
            currentChar == '(' || 
            currentChar == ')' || 
            currentChar == '@' || 
            currentChar == ',' || 
            currentChar == '[' || 
            currentChar == ']' || 
            currentChar == '=') {
            return false; // Forbidden character found, return false
        }
    }
    return true; // No forbidden character found, return true
}