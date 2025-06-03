// Buggy Function
private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = null;
    try {
        output = getOutputStream(); // Buggy Line
        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            FileInputStream input = new FileInputStream(dfosFile);
            try {
                IOUtils.copy(input, output);
                dfosFile.delete();
                dfosFile = null;
            } finally {
                input.close();
            }
        }
    } finally {
        if (output != null) {
            output.close();
        }
    }

    cachedContent = null;
}