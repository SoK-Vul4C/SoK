public static int versionCompare(String fromVersion, String toVersion) {
    String[] fromArr = fromVersion.split("\\.");
    String[] toArr = toVersion.split("\\.");

    if (fromArr.length != 3 || toArr.length != 3) {
        System.err.println("Invalid version format. Version string must consist of three parts separated by periods.");
        return 0; // or handle the error as appropriate
    }

    for (int i = 0; i < 3; i++) {
        int fromPart = Integer.parseInt(fromArr[i]);
        int toPart = Integer.parseInt(toArr[i]);

        if (fromPart != toPart) {
            return Integer.compare(fromPart, toPart);
        }
    }

    return 0; // When all parts are equal
}