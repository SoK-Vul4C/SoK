static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    // Extract the base name without the extension
    String prefix;
    String suffix = "";
    int dot = name.lastIndexOf('.');
    if (dot > 0) {
        prefix = name.substring(0, dot);
        suffix = name.substring(dot);
    } else {
        prefix = name;
    }

    File file = new File(dir, name);

    // Check if the file exists and handle file name collisions
    int index = 2;
    while (file.exists()) {
        file = new File(dir, prefix + "-" + index + suffix);
        index++;
    }

    file.getParentFile().mkdirs();

    return file;
}