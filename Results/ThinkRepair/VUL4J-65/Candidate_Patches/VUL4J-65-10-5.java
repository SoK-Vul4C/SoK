static public File allocateFile(File dir, String name) {
    if (name == null || name.isEmpty()) {
        return null;
    }

    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    File file = new File(dir, name);

    if (file.exists()) {
        int index = 1;
        String fileName = file.getName();
        String baseName;
        String extension = "";

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            baseName = fileName.substring(0, dotIndex);
            extension = fileName.substring(dotIndex);
        } else {
            baseName = fileName;
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