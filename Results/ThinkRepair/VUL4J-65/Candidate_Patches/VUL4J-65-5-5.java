static public File allocateFile(File dir, String name) {
    if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("File name cannot be null or empty.");
    }

    int q = name.indexOf('?');
    String fileName = (q > 0) ? name.substring(0, q) : name;

    File file = new File(dir, fileName);
    String baseName = file.getName();
    String prefix;
    String extension;
    int dotIndex = baseName.lastIndexOf(".");
    
    if (dotIndex != -1) {
        prefix = baseName.substring(0, dotIndex);
        extension = baseName.substring(dotIndex);
    } else {
        prefix = baseName;
        extension = "";
    }

    int index = 1;
    while (file.exists()) {
        file = new File(dir, prefix + "-" + index + extension);
        index++;
    }

    file.getParentFile().mkdirs();

    return file;
}