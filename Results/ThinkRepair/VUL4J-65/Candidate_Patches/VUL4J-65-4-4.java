static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    File file = new File(dir, name);

    if (!file.exists()) {
        return file; // Return original file if it does not exist
    }

    String baseName;
    String extension = "";
    int dot = name.lastIndexOf('.');
    if (dot >= 0) {
        baseName = name.substring(0, dot);
        extension = name.substring(dot);
    } else {
        baseName = name;
    }

    int index = 1;
    do {
        String uniqueName = baseName + "-" + index + extension;
        file = new File(dir, uniqueName);
        index++;
    } while (file.exists());

    file.getParentFile().mkdirs();

    return file;
}