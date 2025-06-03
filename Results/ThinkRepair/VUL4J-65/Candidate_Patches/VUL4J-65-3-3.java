static public File allocateFile(File dir, String name) {
    int q = name.indexOf('?');
    if (q > 0) {
        name = name.substring(0, q);
    }

    File file = new File(dir, name);

    if (file.exists()) {
        int count = 1;
        String baseName = name;
        String extension = "";

        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = name.substring(0, dotIndex);
            extension = name.substring(dotIndex);
        }

        while (file.exists()) {
            file = new File(dir, baseName + "_" + count + extension);
            count++;
        }
    }

    file.getParentFile().mkdirs();

    return file;
}