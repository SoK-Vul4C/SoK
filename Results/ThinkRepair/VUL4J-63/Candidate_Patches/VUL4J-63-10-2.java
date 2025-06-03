public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    int minLength = Math.min(fromArr.length, toArr.length);

    for (int i = 0; i < minLength; i++) {
        int fromPart = Integer.parseInt(fromArr[i]);
        int toPart = Integer.parseInt(toArr[i]);
        
        if (fromPart != toPart) {
            return fromPart - toPart;
        }
    }

    return fromArr.length - toArr.length;
}