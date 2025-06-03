public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    try {
        int fromFirst = (fromArr.length >= 1) ? Integer.parseInt(fromArr[0]) : 0;
        int fromMiddle = (fromArr.length >= 2) ? Integer.parseInt(fromArr[1]) : 0;
        int fromEnd = (fromArr.length >= 3) ? Integer.parseInt(fromArr[2]) : 0;

        int toFirst = (toArr.length >= 1) ? Integer.parseInt(toArr[0]) : 0;
        int toMiddle = (toArr.length >= 2) ? Integer.parseInt(toArr[1]) : 0;
        int toEnd = (toArr.length >= 3) ? Integer.parseInt(toArr[2]) : 0;

        if (fromFirst != toFirst) {
            return Integer.compare(fromFirst, toFirst);
        } else if (fromMiddle != toMiddle) {
            return Integer.compare(fromMiddle, toMiddle);
        } else {
            return Integer.compare(fromEnd, toEnd);
        }
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
        e.printStackTrace();
        return 0; // Or any appropriate error handling
    }
}