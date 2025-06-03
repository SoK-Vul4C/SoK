public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    if (fromArr.length != 3 || toArr.length != 3) {
        System.err.println("Invalid version format. Version string must consist of three parts separated by periods.");
        return 0; // or handle the error as appropriate
    }

    try {
        int fromFirst = Integer.parseInt(fromArr[0]);
        int fromMiddle = Integer.parseInt(fromArr[1]);
        int fromEnd = Integer.parseInt(fromArr[2]);
        int toFirst = Integer.parseInt(toArr[0]);
        int toMiddle = Integer.parseInt(toArr[1]);
        int toEnd = Integer.parseInt(toArr[2]);

        if (fromFirst != toFirst) {
            return Integer.compare(fromFirst, toFirst);
        } else if (fromMiddle != toMiddle) {
            return Integer.compare(fromMiddle, toMiddle);
        } else {
            return Integer.compare(fromEnd, toEnd);
        }
    } catch (NumberFormatException e) {
        System.err.println("Invalid version format: " + e.getMessage());
        return 0; // or handle the error as appropriate
    }
}