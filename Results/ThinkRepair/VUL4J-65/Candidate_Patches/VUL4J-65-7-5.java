static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    // Extract the base file name without the extension
    String fileName = new File(name).getName();
    String fileExtension = "";
    int dotIndex = fileName.lastIndexOf('.');
    String fileWithoutExtension = fileName;

    if (dotIndex > 0) {
        fileWithoutExtension = fileName.substring(0, dotIndex);
        fileExtension = fileName.substring(dotIndex);
    }

    File file = new File(dir, fileName);

    // Handle file name conflicts
    int index = 1;
    while (file.exists()) {
        String indexedName = fileWithoutExtension + "-" + index + fileExtension;
        file = new File(dir, indexedName);
        index++;
    }

    file.getParentFile().mkdirs();

    return file;
}