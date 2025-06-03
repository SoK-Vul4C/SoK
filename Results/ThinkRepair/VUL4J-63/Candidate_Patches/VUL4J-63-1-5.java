public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    for (int i = 0; i < Math.max(fromArr.length, toArr.length); i++) {
        int fromValue = (i < fromArr.length) ? Integer.parseInt(fromArr[i]) : 0;
        int toValue = (i < toArr.length) ? Integer.parseInt(toArr[i]) : 0;

        if (fromValue < toValue) {
            return -1;
        } else if (fromValue > toValue) {
            return 1;
        }
    }

    if (fromArr.length < toArr.length) {
        return -1; // fromVersion has fewer components, so it is considered lower
    } else if (fromArr.length > toArr.length) {
        return 1; // fromVersion has more components, so it is considered higher
    }

    return 0; // Versions are equal
}