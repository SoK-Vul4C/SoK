static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    File file = new File(dir, name);

    if (file.exists()) {
        int dotIndex = name.lastIndexOf('.');
        String prefix = name.substring(0, dotIndex);
        String suffix = name.substring(dotIndex);
        
        int index = 2;
        while (file.exists()) {
            file = new File(dir, prefix + "-" + index + suffix);
            index++;
        }
    }

    file.getParentFile().mkdirs();

    return file;
}