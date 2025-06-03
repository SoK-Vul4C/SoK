private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    try (OutputStream output = getOutputStream()) {
        if (output != null) {
            if (cachedContent != null) {
                output.write(cachedContent);
            } else {
                try (FileInputStream input = new FileInputStream(dfosFile)) {
                    IOUtils.copy(input, output);
                }
                dfosFile.delete();
                dfosFile = null;
            }
        }
    } catch (IOException e) {
        // Handle any IO exceptions
        e.printStackTrace();
        throw e;
    }

    cachedContent = null;
}