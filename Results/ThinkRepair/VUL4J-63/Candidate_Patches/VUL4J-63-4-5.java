public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    for (int i = 0; i < Math.max(fromArr.length, toArr.length); i++) {
        int fromSeg = i < fromArr.length ? getVersionSegmentValue(fromArr[i]) : 0;
        int toSeg = i < toArr.length ? getVersionSegmentValue(toArr[i]) : 0;

        if (fromSeg < toSeg) {
            return -1;
        } else if (fromSeg > toSeg) {
            return 1;
        }
    }

    return 0;
}

private static int getVersionSegmentValue(String segment) {
    try {
        return Integer.parseInt(segment);
    } catch (NumberFormatException e) {
        // For non-numeric segments, return a large value based on segment content
        return segment.length() == 0 ? Integer.MIN_VALUE : -segment.charAt(0);
    }
}