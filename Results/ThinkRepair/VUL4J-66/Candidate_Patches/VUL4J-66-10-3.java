protected static boolean isValid(String str) {
    if (str == null || str.isEmpty()) {
        return false;
    }

    for (int i = 0; i < str.length(); i++) {
        if (isSpecialCharacter(str.charAt(i))) {
            return false;
        }
    }

    return true;
}

private static boolean isSpecialCharacter(char ch) {
    String specialCharacters = "/\\?:<>';(@,[=]";
    return specialCharacters.indexOf(ch) != -1;
}