public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    // Default values if version string does not contain 3 parts
    int fromFirst = 0;
    int fromMiddle = 0;
    int fromEnd = 0;
    int toFirst = 0;
    int toMiddle = 0;
    int toEnd = 0;

    try {
        if (fromArr.length >= 3) {
            fromFirst = Integer.parseInt(fromArr[0]);
            fromMiddle = Integer.parseInt(fromArr[1]);
            fromEnd = Integer.parseInt(fromArr[2]);
        }

        if (toArr.length >= 3) {
            toFirst = Integer.parseInt(toArr[0]);
            toMiddle = Integer.parseInt(toArr[1]);
            toEnd = Integer.parseInt(toArr[2]);
        }
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
        // Handle parsing errors or index out of bounds exceptions
        e.printStackTrace();
        return 0; // Or any appropriate error handling
    }

    if (fromFirst != toFirst) {
        return Integer.compare(fromFirst, toFirst);
    } else if (fromMiddle != toMiddle) {
        return Integer.compare(fromMiddle, toMiddle);
    } else {
        return Integer.compare(fromEnd, toEnd);
    }
}