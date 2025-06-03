public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    try {
        for (int i = 0; i < 3; i++) {
            int fromPart = (i < fromArr.length) ? Integer.parseInt(fromArr[i]) : 0;
            int toPart = (i < toArr.length) ? Integer.parseInt(toArr[i]) : 0;

            if (fromPart < toPart) {
                return -1;
            } else if (fromPart > toPart) {
                return 1;
            }
        }
    } catch (NumberFormatException e) {
        // handle invalid version format
        return 0; // or any custom handling based on the use case
    }

    return 0; // versions are equal
}