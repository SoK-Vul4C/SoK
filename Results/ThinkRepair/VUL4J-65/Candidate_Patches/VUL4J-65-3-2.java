static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    File file = new File(dir, name);

    int index = 1;
    while (file.exists()) {
        int dot = name.lastIndexOf('.');
        String prefix = (dot == -1) ? name : name.substring(0, dot);
        String suffix = (dot == -1) ? "" : name.substring(dot);
        
        file = new File(dir, prefix + "-" + index + suffix);
        index++;
    }
    
    file.getParentFile().mkdirs();

    return file;
}