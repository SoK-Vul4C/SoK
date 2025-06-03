public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    int fromFirst = 0, fromMiddle = 0, fromEnd = 0;
    int toFirst = 0, toMiddle = 0, toEnd = 0;

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