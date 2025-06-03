static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    File file = new File(dir, name);
    
    // Ensure the parent directories exist
    if (file.getParentFile() != null && !file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
    }
    
    int dot = name.lastIndexOf('.');
    String prefix = (dot < 0) ? name : name.substring(0, dot);
    String suffix = (dot < 0) ? "" : name.substring(dot);
    int index = 2;
    while (file.exists()) {
        file = new File(dir, prefix + "-" + index++ + suffix);
    }
    
    return file;
}