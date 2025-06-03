static public File allocateFile(File dir, String name) {
    // Remove any characters after the question mark '?'
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    File file = new File(dir, name);

    // Handle case where file already exists
    if (file.exists()) {
        int index = 1;
        String baseName = name;
        String extension = "";

        int dotIndex = name.lastIndexOf('.');
        if (dotIndex != -1) {
            baseName = name.substring(0, dotIndex);
            extension = name.substring(dotIndex);
        }

        do {
            String newName = baseName + "-" + index + extension;
            file = new File(dir, newName);
            index++;
        } while (file.exists());
    }

    file.getParentFile().mkdirs();

    return file;
}