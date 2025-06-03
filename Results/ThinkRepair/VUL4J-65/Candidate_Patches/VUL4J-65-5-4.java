static public File allocateFile(File dir, String name) {
    if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("File name cannot be null or empty.");
    }

    int q = name.indexOf('?');
    String fileName = q > 0 ? name.substring(0, q) : name;

    File file = new File(dir, fileName);
    String baseName = file.getName();
    String prefix = baseName;
    String suffix = "";
    int index = 1;

    while (file.exists()) {
        int dotIndex = baseName.lastIndexOf(".");
        prefix = dotIndex != -1 ? baseName.substring(0, dotIndex) : baseName;
        suffix = dotIndex != -1 ? baseName.substring(dotIndex) : "";
        file = new File(dir, prefix + "-" + index + suffix);
        index++;
    }

    file.getParentFile().mkdirs();

    return file;
}