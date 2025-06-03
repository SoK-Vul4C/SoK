static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    File file = new File(dir, name);

    if (!file.exists()) {
        return file;
    }

    String baseName = name;
    String extension = "";
    int dot = name.lastIndexOf('.');
    if (dot >= 0) {
        baseName = name.substring(0, dot);
        extension = name.substring(dot);
    }

    // Check for unique file name
    int index = 1;
    do {
        String newName = baseName + "-" + index + extension;
        file = new File(dir, newName);
        index++;
    } while (file.exists());

    file.getParentFile().mkdirs();
    
    return file;
}