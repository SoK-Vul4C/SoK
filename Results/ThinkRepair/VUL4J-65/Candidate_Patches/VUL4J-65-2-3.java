static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    File file = new File(dir, name);
    
    int dot = name.lastIndexOf('.');
    String prefix = (dot < 0) ? name : name.substring(0, dot);
    String suffix = (dot < 0) ? "" : name.substring(dot);
    
    int index = 1;
    while (file.exists()) {
        index++;
        String newFileName = prefix + "-" + index + suffix;
        file = new File(dir, newFileName);
    }
    
    file.getParentFile().mkdirs();
    
    return file;
}