static public File allocateFile(File dir, String name) {
    if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("File name cannot be null or empty.");
    }

    int q = name.indexOf('?');
    if (q >= 0) {
        name = name.substring(0, q);
    }

    File file = new File(dir, name);
    String baseName = file.getName();
    String prefix = baseName;
    String suffix = "";
    int index = 1;

    while (file.exists()) {
        int lastIndex = baseName.lastIndexOf('.');
        if (lastIndex >= 0) {
            prefix = baseName.substring(0, lastIndex);
            suffix = baseName.substring(lastIndex);
        }
        file = new File(dir, prefix + "-" + index + suffix);
        index++;
    }

    file.getParentFile().mkdirs();

    return file;
}