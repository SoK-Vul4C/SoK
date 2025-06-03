public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegexExpression)) {
        return (String) parse(urlRegexExpression, String.class);
    } else if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        return "^(https?|ftp)://([a-z0-9$_.+!*'(),;?&=-]|%[0-9a-fA-F]{2})+" +
               "(:([a-z0-9$_.+!*'(),;?&=-]|%[0-9a-fA-F]{2})+)?" +
               "@)?(#?)" +
               "((([a-z0-9].|[a-z0-9][a-z0-9-]*[a-z0-9].)*" +
               "[a-z][a-z0-9-]*[a-z0-9]" +
               "|((\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5]).){3}" +
               "(\\d|[1-9]\\d|1\\d{2}|2[0-4][0-9]|25[0-5])" +
               ")(:\\d+)?" +
               "((/([a-z0-9$_.+!*'(),;:@&=-]|%[0-9a-fA-F]{2})*)*" + // Corrected Line for path
               "(\\?([a-z0-9$_.+!*'(),;:@&=-]|%[0-9a-fA-F]{2})*" + // Corrected Line for query parameters
               ")?(#[a-z0-9_.~:/?#[\\]@!$&'()*+,;=%]+)?)?" + // Corrected Line for fragment
               "$";
    }
}