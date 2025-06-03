public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegexExpression)) {
        return (String) parse(urlRegexExpression, String.class);
    } else if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        // Regular expression for basic URL validation
        return "^(https?|ftp)://[a-zA-Z0-9.]+(:\\d{1,5})?(/\\S*)?$";
    }
}
```

In this corrected version, the regular expression `^(https?|ftp)://[a-zA-Z0-9.]+(:\\d{1,5}