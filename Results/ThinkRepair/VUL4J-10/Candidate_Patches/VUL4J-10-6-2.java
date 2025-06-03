private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (dfosFile != null) {
        OutputStream output = getOutputStream();

        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            FileInputStream input = new FileInputStream(dfosFile);
            try {
                IOUtils.copy(input, output);
            } finally {
                input.close();
            }
            dfosFile.delete();
            dfosFile = null;
        }

        output.close();
    } else {
        cachedContent = null;
    }
}