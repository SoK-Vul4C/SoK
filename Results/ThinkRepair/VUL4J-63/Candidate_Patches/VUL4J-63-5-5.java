public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    for (int i = 0; i < Math.max(fromArr.length, toArr.length); i++) {
        int fromVal = i < fromArr.length ? tryParseInt(fromArr[i]) : 0;
        int toVal = i < toArr.length ? tryParseInt(toArr[i]) : 0;

        if (fromVal < toVal) {
            return -1;
        } else if (fromVal > toVal) {
            return 1;
        }
    }

    return 0;
}

private static int tryParseInt(String value) {
    try {
        return Integer.parseInt(value);
    } catch (NumberFormatException e) {
        return 0;
    }
}