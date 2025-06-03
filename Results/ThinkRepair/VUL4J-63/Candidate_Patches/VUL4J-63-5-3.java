public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    int maxLength = Math.max(fromArr.length, toArr.length);

    for (int i = 0; i < maxLength; i++) {
        int fromVal = i < fromArr.length ? tryParseInt(fromArr[i]) : 0;
        int toVal = i < toArr.length ? tryParseInt(toArr[i]) : 0;

        if (fromVal != toVal) {
            return Integer.compare(fromVal, toVal);
        }
    }

    return 0;
}

private static int tryParseInt(String value) {
    try {
        return Integer.parseInt(value);
    } catch (NumberFormatException e) {
        // Return 0 if parsing fails
        return 0;
    }
}