static public File allocateFile(File dir, String name) {
    String actualName = name.split("\\?")[0]; // Remove query string if it exists

    File file = new File(dir, actualName);

    int dot = actualName.indexOf('.');
    String prefix = dot < 0 ? actualName : actualName.substring(0, dot);
    String suffix = dot < 0 ? "" : actualName.substring(dot);
    int index = 2;
    while (file.exists()) {
        file = new File(dir, prefix + "-" + index++ + suffix);
    }

    file.getParentFile().mkdirs();

    return file;
}