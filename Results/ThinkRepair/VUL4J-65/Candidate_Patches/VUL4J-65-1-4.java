static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }
    
    // Ensure the directory exists or create it
    if (!dir.exists()) {
        dir.mkdirs();
    }
    
    File file = new File(dir, name);
    
    if (file.exists()) {
        String baseName;
        String extension = "";
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex == -1) {
            baseName = name;
        } else {
            baseName = name.substring(0, dotIndex);
            extension = name.substring(dotIndex);
        }

        int index = 1;
        do {
            file = new File(dir, baseName + "-" + index + extension);
            index++;
        } while (file.exists());
    }

    return file;
}