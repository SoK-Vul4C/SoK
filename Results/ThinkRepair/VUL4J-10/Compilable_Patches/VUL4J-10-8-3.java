// Buggy Function
private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = getOutputStream(); // Buggy Line
    if (cachedContent != null) {
        output.write(cachedContent);
    } else {
        FileInputStream input = new FileInputStream(dfosFile);
        try {
            IOUtils.copy(input, output);
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            dfosFile.delete();
            dfosFile = null;
        }
    }

    cachedContent = null;
}