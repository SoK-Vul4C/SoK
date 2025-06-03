private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = null;

    try {
        output = getOutputStream();

        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            try (FileInputStream input = new FileInputStream(dfosFile)) {
                IOUtils.copy(input, output);
            }
            dfosFile.delete();
        }

        cachedContent = null;
    } finally {
        if (output != null) {
            output.close();
        }
    }
}