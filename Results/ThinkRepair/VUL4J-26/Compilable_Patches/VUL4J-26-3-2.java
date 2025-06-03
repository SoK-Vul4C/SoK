public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegexExpression)) {
        return (String) parse(urlRegexExpression, String.class);
    } else if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        // Simplified regular expression for URL validation
        return "^https?://.*$";
    }
}