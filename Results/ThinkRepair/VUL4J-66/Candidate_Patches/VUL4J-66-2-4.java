protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    String invalidChars = "/\\?:;()@,[]=";
    
    for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        for (int j = 0; j < invalidChars.length(); j++) {
            if (c == invalidChars.charAt(j)) {
                return false; // Invalid character found, string is not valid
            }
        }
    }
    
    return true; // No invalid characters found in the string
}