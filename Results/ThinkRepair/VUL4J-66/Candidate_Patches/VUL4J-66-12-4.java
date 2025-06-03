// Fixed Function
protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false; // Empty or null string is not valid
    }

    for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        if (!((c >= '\u0020' && c <= '\u0021') || (c >= '\u0023' && c <= '\u002B') ||
              (c >= '\u002D' && c <= '\u007E'))) {
            return false; // Special character found, return false immediately
        }
    }

    return true; // No special characters found, return true
}