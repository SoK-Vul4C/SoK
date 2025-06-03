public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    for (int i = 0; i < Math.max(fromArr.length, toArr.length); i++) {
        int fromSeg = i < fromArr.length ? Integer.parseInt(fromArr[i]) : 0;
        int toSeg = i < toArr.length ? Integer.parseInt(toArr[i]) : 0;
        
        if (fromSeg != toSeg) {
            return fromSeg - toSeg;
        }
    }
    
    return 0;
}