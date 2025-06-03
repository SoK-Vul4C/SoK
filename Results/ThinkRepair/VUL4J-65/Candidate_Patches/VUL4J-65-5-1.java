static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    File file = new File(dir, name);
    
    int dot = name.lastIndexOf('.');
    String prefix = name;
    String suffix = "";
    if (dot >= 0) {
        prefix = name.substring(0, dot);
        suffix = name.substring(dot);
    }
    
    int index = 1;
    while (file.exists()) {
        file = new File(dir, prefix + "-" + index + suffix);
        index++;
    }
    
    file.getParentFile().mkdirs();
    
    return file;
}