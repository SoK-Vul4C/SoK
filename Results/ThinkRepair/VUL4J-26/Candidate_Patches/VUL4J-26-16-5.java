public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        return "^(https?|ftp):\\/\\/(?:[a-zA-Z0-9]+\\.){1,}[a-zA-Z]{2,}(?::\\d{1,5})?(?:\\/[\\/|\\w|\\s|.|,|?|&|=|-]*)?$";
    }
}