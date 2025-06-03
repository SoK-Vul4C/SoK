public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    int maxLength = Math.max(fromArr.length, toArr.length);

    for (int i = 0; i < maxLength; i++) {
        int fromPart = (i < fromArr.length) ? Integer.parseInt(fromArr[i]) : 0;
        int toPart = (i < toArr.length) ? Integer.parseInt(toArr[i]) : 0;

        if (fromPart < toPart) {
            return -1;
        } else if (fromPart > toPart) {
            return 1;
        }
    }

    if (fromArr.length < toArr.length) {
        return -1;
    } else if (fromArr.length > toArr.length) {
        return 1;
    }

    return 0;
}