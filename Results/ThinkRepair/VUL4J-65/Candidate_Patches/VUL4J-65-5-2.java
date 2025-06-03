static public File allocateFile(File dir, String name) {
    if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("File name cannot be null or empty.");
    }

    int q = name.indexOf('?');
    String baseName = q > 0 ? name.substring(0, q) : name;

    File file = new File(dir, baseName);
    String prefix = baseName;
    String suffix = "";
    int index = 1;

    while (file.exists()) {
        int dot = baseName.lastIndexOf('.');
        prefix = dot >= 0 ? baseName.substring(0, dot) : baseName;
        suffix = dot >= 0 ? baseName.substring(dot) : "";
        file = new File(dir, prefix + "-" + index + suffix);
        index++;
    }

    file.getParentFile().mkdirs();

    return file;
}