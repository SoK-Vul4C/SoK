public String getUrlRegex() {
    if (StringUtils.isNotEmpty(urlRegex)) {
        return urlRegex;
    } else {
        return "^https?:\\/\\/(?:(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(?::\\d{1,5})?|localhost)(?:\\/[^\\s]*)?$";
    }
}