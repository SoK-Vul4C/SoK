private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (getOutputStream() == null) {
        // If output stream is not available, handle the situation accordingly
        cachedContent = null;
        return;
    }

    try (OutputStream output = getOutputStream()) {
        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            try (FileInputStream input = new FileInputStream(dfosFile)) {
                IOUtils.copy(input, output);
            }
            dfosFile.delete();
            dfosFile = null;
        }
    } catch (IOException e) {
        // Rethrow the IOException to match the expected exception
        throw new IOException("Failed to read/write from output stream", e);
    }

    cachedContent = null;
}