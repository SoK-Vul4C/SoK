private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = getOutputStream(); // Buggy Line
    if (cachedContent != null) {
        if (output != null) {
            output.write(cachedContent);
            output.close();
        }
    } else {
        if (output != null) {
            FileInputStream input = new FileInputStream(dfosFile);
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
            }
            dfosFile.delete();
            dfosFile = null;
            output.close();
        }
    }

    cachedContent = null;
}