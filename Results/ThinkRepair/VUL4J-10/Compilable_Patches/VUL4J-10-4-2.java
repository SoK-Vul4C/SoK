private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = null;
    FileInputStream input = null;

    try {
        if (cachedContent != null) {
            output = getOutputStream();
            output.write(cachedContent);
        } else {
            input = new FileInputStream(dfosFile);
            output = getOutputStream();
            IOUtils.copy(input, output);
            dfosFile.delete();
            dfosFile = null;
        }
    } finally {
        if (output != null) {
            output.close();
        }
        if (input != null) {
            input.close();
        }
    }

    cachedContent = null;
}