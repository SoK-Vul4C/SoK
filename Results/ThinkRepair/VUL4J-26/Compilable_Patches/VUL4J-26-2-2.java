public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegexExpression)) {
        return (String) parse(urlRegexExpression, String.class);
    } else if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        return "^(https?|ftp):\\/\\/" +
                "([a-zA-Z0-9]+([a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+" + // Domain name
                "([a-zA-Z]{2,}|(\\d{1,3}\\.){3}\\d{1,3})" + // Top-level domain or IP
                "(:\\d+)?" + // Port
                "(/[^\\s]*)?$"; // Path
    }
}