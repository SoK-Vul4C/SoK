public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    for (int i = 0; i < Math.max(fromArr.length, toArr.length); i++) {
        int fromPart = i < fromArr.length ? Integer.parseInt(fromArr[i]) : 0;
        int toPart = i < toArr.length ? Integer.parseInt(toArr[i]) : 0;

        if (fromPart != toPart) {
            return fromPart - toPart;
        }
    }

    return 0;
}