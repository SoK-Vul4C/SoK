static public File allocateFile(File dir, String name) {
    // Check if the name contains a query string
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q); // Exclude query string from the file name
    }

    File file = new File(dir, name);

    // Check for file name conflicts
    int index = 1;
    String baseName = name;
    String extension = "";
    
    // Split the name into baseName and extension if there is a file extension
    int lastDotIndex = name.lastIndexOf('.');
    if (lastDotIndex != -1) {
        baseName = name.substring(0, lastDotIndex);
        extension = name.substring(lastDotIndex);
    }

    // Handle file name conflicts by appending index to the baseName
    while (file.exists()) {
        file = new File(dir, baseName + "-" + index + extension);
        index++;
    }

    // Create parent directories if they do not exist
    file.getParentFile().mkdirs();

    return file;
}