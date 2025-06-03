static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    // Get the base name without the extension
    int dot = name.lastIndexOf('.');
    String prefix = dot < 0 ? name : name.substring(0, dot);
    String suffix = dot < 0 ? "" : name.substring(dot);
    
    File file = new File(dir, name); // Corrected Line
    
    int index = 2;
    while (file.exists()) {
        file = new File(dir, prefix + "-" + index++ + suffix);
    }
    
    file.getParentFile().mkdirs();
    
    return file;
}