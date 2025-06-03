public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegexExpression)) {
        return (String) parse(urlRegexExpression, String.class);
    } else if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        return "^(https?|ftp):\\/\\/" +
                "(([a-z0-9$_\\.\\+!\\*\\'\\(\\),;\\?&=-]|%[0-9a-f]{2})+" +
                "(:([a-z0-9$_\\.\\+!\\*\\'\\(\\),;\\?&=-]|%[0-9a-f]{2})+)?" +
                "@)?" +
                "([a-zA-Z0-9.-]+|" +
                "(\\d{1,3}\\.){3}\\d{1,3})" + // IP Address pattern
                "(\\:\\d+)?" +
                "((\\/|" +
                "(\\/(([a-z0-9$_\\.\\+!\\*\\'\\(\\),;:@&=-]|%[0-9a-f]{2})*)*){0,1}" +
                "(\\?([a-z0-9$_\\.\\+!\\*\\'\\(\\),;:@&=-]|%[0-9a-f]{2})*){0,1}" +
                "?)?)?" +
                "(#([a-z0-9$_\\.\\+!\\*\\'\\(\\),;:@&=-]|%[0-9a-f]{2})*)?" +
                "$";
    }
}