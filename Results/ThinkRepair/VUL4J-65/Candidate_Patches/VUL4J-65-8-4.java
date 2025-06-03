static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    if (name.isBlank()) {
        throw new IllegalArgumentException("File name cannot be empty");
    }
    
    int dot = name.lastIndexOf('.');
    String prefix = (dot < 0 || dot == name.length() - 1) ? name : name.substring(0, dot);
    String suffix = (dot < 0 || dot == name.length() - 1) ? "" : name.substring(dot);
    int index = 2;
    
    File file = new File(dir, name);
    while (file.exists()) {
        file = new File(dir, prefix + "-" + index++ + suffix);
    }
    
    file.getParentFile().mkdirs();
    
    return file;
}