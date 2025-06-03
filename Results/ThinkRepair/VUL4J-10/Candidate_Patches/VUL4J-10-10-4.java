private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    FileOutputStream output = null;
    FileInputStream input = null;
    try {
        if (cachedContent != null) {
            output = new FileOutputStream(getTempFile());
            output.write(cachedContent);
        } else {
            input = new FileInputStream(dfosFile);
            output = new FileOutputStream(getTempFile());
            IOUtils.copy(input, output);
            dfosFile.delete();
            dfosFile = null;
        }
    } finally {
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }

    cachedContent = null;
}