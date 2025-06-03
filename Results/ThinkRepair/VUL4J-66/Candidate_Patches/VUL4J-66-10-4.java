protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    String specialCharacters = "/\\?:<>';(@,[=]";
    
    for (int i = 0; i < str.length(); i++) {
        char ch = str.charAt(i);
        if (specialCharacters.indexOf(ch) != -1) {
            return false;
        }
    }

    return true;
}