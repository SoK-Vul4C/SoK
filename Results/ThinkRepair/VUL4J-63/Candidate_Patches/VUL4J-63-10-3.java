public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    int minLength = Math.min(fromArr.length, toArr.length);

    for (int i = 0; i < minLength; i++) {
        int fromPart = Integer.parseInt(fromArr[i]);
        int toPart = Integer.parseInt(toArr[i]);
        
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