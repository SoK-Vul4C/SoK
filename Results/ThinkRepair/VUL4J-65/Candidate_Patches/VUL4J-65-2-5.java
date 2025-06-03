static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    File file = new File(dir, name);
    
    if (!file.exists()) {
        file.getParentFile().mkdirs();
        return file;
    }
    
    String baseName = name;
    int index = 1;
    
    String fileName;
    
    do {
        fileName = baseName + "-" + index;
        file = new File(dir, fileName);
        index++;
    } while (file.exists());
    
    file.getParentFile().mkdirs();
    
    return file;
}