public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegexExpression)) {
        return (String) parse(urlRegexExpression, String.class);
    } else if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        return "^(https?|ftp)://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(:[0-9]+)?(/.*)?$";
    }
}