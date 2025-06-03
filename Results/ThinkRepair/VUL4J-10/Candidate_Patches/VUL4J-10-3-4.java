private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    ByteArrayOutputStream output = new ByteArrayOutputStream();

    if (cachedContent != null) {
        output.write(cachedContent);
    } else {
        FileInputStream input = null;
        try {
            input = new FileInputStream(dfosFile);
            IOUtils.copy(input, output);
            dfosFile.delete();
            dfosFile = null;
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    setOutputStream(output);
    cachedContent = null;
}