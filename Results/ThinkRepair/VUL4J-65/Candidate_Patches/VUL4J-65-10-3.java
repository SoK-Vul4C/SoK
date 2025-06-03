static public File allocateFile(File dir, String name) {
    if (name == null || name.isEmpty()) {
        return null;
    }

    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    File file = new File(dir, name);

    String fileName = file.getName();
    String fileExtension = "";
    int dotIndex = fileName.lastIndexOf('.');
    if(dotIndex >= 0) {
        fileExtension = fileName.substring(dotIndex);
        fileName = fileName.substring(0, dotIndex);
    }

    int index = 1;
    while (file.exists()) {
        String newName = fileName + "-" + index + fileExtension;
        file = new File(dir, newName);
        index++;
    }

    file.getParentFile().mkdirs();

    return file;
}