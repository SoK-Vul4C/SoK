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
    String nameWithoutExtension = "";
    String extension = "";
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex != -1) {
        nameWithoutExtension = fileName.substring(0, dotIndex);
        extension = fileName.substring(dotIndex);
    } else {
        nameWithoutExtension = fileName;
    }

    int index = 1;
    while (file.exists()) {
        file = new File(dir, nameWithoutExtension + "-" + index + extension);
        index++;
    }

    file.getParentFile().mkdirs();

    return file;
}