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
    
    // Handle the case where the file already exists
    if (file.exists()) {
        int index = 1;
        String fileName = name;
        int dotIndex = name.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : name.substring(dotIndex);
        
        do {
            index++;
            file = new File(dir, baseName + "-" + index + extension);
        } while (file.exists());
    }

    return file;
}