static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    File file = new File(dir, name);

    // Extract the base file name without the extension
    int dotIndex = name.lastIndexOf('.');
    String prefix = (dotIndex > 0) ? name.substring(0, dotIndex) : name;
    String extension = (dotIndex > 0) ? name.substring(dotIndex) : "";

    // Handle file name conflicts
    int index = 1;
    while (file.exists()) {
        String indexedName = String.format("%s-%d%s", prefix, index, extension);
        file = new File(dir, indexedName);
        index++;
    }

    file.getParentFile().mkdirs();

    return file;
}