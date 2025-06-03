private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    InputStream input = getInputStream(); // Fixed Line
    if (cachedContent != null) {
        input.read(cachedContent);
    } else {
        try (FileInputStream fileInput = new FileInputStream(dfosFile)) {
            IOUtils.copy(fileInput, input);
            dfosFile.delete();
            dfosFile = null;
        }
    }
    input.close();

    cachedContent = null;
}