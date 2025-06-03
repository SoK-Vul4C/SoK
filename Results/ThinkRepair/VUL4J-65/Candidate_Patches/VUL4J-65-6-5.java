static public File allocateFile(File dir, String name) {
    // Remove any characters after the question mark '?'
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    File file = new File(dir, name);

    // Handle case where file already exists
    if (file.exists()) {
        String baseName;
        String extension;
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex < 0) {
            baseName = name;
            extension = "";
        } else {
            baseName = name.substring(0, dotIndex);
            extension = name.substring(dotIndex);
        }

        int index = 2;
        String newFileName = name;
        while (file.exists()) {
            newFileName = baseName + "-" + index + extension;
            file = new File(dir, newFileName);
            index++;
        }
    }

    // Create file and its parent directories if they do not exist
    try {
        file.getParentFile().mkdirs();
        file.createNewFile(); // Create a new file
    } catch (IOException e) {
        e.printStackTrace();
    }

    return file;
}