public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    for (int i = 0; i < Math.max(fromArr.length, toArr.length); i++) {
        int fromSeg = i < fromArr.length ? parseVersionSegment(fromArr[i]) : 0;
        int toSeg = i < toArr.length ? parseVersionSegment(toArr[i]) : 0;
        
        if (fromSeg < toSeg) {
            return -1;
        } else if (fromSeg > toSeg) {
            return 1;
        }
    }
    
    return 0;
}

private static int parseVersionSegment(String segment) {
    try {
        return Integer.parseInt(segment);
    } catch (NumberFormatException e) {
        if (segment.matches("[0-9]+")) {
            // If the segment contains only digits, return a large value
            return Integer.MAX_VALUE;
        } else {
            // If the segment contains non-numeric characters, return 0
            return 0;
        }
    }
}