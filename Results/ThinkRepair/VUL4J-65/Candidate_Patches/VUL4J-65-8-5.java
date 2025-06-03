static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    if (name.isBlank()) {
        throw new IllegalArgumentException("File name cannot be empty");
    }
    
    if (!name.matches("[a-zA-Z0-9_.-]+")) {
        throw new IllegalArgumentException("Invalid characters in file name");
    }
    
    String baseName = name;
    int dotIndex = name.lastIndexOf('.');
    
    String prefix = dotIndex < 0 ? name : name.substring(0, dotIndex);
    String extension = dotIndex < 0 ? "" : name.substring(dotIndex);
    
    File file = new File(dir, name);
    int index = 1;

    while (file.exists()) {
        name = prefix + "-" + index + extension;
        file = new File(dir, name);
        index++;
    }

    file.getParentFile().mkdirs();
    
    return file;
}