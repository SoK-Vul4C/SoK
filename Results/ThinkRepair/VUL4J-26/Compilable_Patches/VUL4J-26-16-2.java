public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegexExpression)) {
        return (String) parse(urlRegexExpression, String.class);
    } else if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        return "^(https?|ftp):\\/\\/" +
                "(([a-z0-9$_\\.\\+!\\*\\'\\(\\),;\\?&=-]|%[0-9a-f]{2})+" +
                "(:([a-z0-9$_\\.\\+!\\*\\'\\(\\),;\\?&=-]|%[0-9a-f]{2})+)?" +
                "@)?(#?" +
                ")([a-z0-9]+([\\-\\.][a-z0-9]+)*\\.[a-z]{2,6}" +
                "|(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)" +
                ")(:\\d+)?" +
                "([\\/\\?\\=\\&\\~\\.\\+\\-;\\%_]*)" +
                "(#[a-z0-9]+)?" +
                "$";
    }
}