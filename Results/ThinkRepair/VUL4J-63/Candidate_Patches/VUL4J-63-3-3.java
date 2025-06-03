public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    try {
        if (fromArr.length < 3 || toArr.length < 3) {
            throw new IllegalArgumentException("Invalid version format");
        }

        int fromFirst = Integer.parseInt(fromArr[0]);
        int fromMiddle = Integer.parseInt(fromArr[1]);
        int fromEnd = Integer.parseInt(fromArr[2]);

        int toFirst = Integer.parseInt(toArr[0]);
        int toMiddle = Integer.parseInt(toArr[1]);
        int toEnd = Integer.parseInt(toArr[2]);

        if (fromFirst != toFirst) {
            return fromFirst - toFirst;
        } else if (fromMiddle != toMiddle) {
            return fromMiddle - toMiddle;
        } else {
            return fromEnd - toEnd;
        }
    } catch (NumberFormatException e) {
        // handle invalid version format
        return 0; // or any custom handling based on the use case
    }
}