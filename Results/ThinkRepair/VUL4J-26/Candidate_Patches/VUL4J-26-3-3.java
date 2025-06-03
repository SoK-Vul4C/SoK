public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegexExpression)) {
        return (String) parse(urlRegexExpression, String.class);
    } else if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        // Regular expression for URL validation
        return "^(https?|ftp)://[a-z0-9-]+(\\.[a-z0-9-]+)*(:\\d{1,5})?(/[a-zA-Z0-9-._?,'+&%$#=~]*)?$";
    }
}
```

In this updated version, the regular expression `^(https?|ftp)://[a-z0-9-]+(\\.[a-z0-9-]+)*(:\\d{1,5}