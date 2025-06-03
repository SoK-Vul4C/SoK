public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegexExpression)) {
        return (String) parse(urlRegexExpression, String.class);
    } else if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        return "^(https?|ftp):\\/\\/" +
                "([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*" +  // Domain name
                "(\\.[a-zA-Z]{2,})" +                 // Top-level domain
                "(:[0-9]+)?" +                         // Port number
                "(/[a-zA-Z0-9_.%\\-]*)*" +             // Path
                "(\\?[a-zA-Z0-9_=%&]*)?$";             // Query parameters
    }
}