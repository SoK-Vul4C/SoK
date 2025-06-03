static public File allocateFile(File dir, String name) {
    // Remove any characters after the question mark '?'
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    int index = 2;
    String fileName = name;
    File file = new File(dir, fileName);

    // Check if file already exists, modify the file name if needed
    while (file.exists()) {
        int dot = name.lastIndexOf('.');
        String prefix = dot < 0 ? name : name.substring(0, dot);
        String suffix = dot < 0 ? "" : name.substring(dot);

        fileName = prefix + "-" + index++ + suffix;
        file = new File(dir, fileName);
    }

    file.getParentFile().mkdirs();

    return file;
}