private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (cachedContent != null) {
        OutputStream output = getOutputStream();
        if (output != null) {
            output.write(cachedContent);
            output.close();
        }
    } else {
        if (dfosFile != null) {
            FileInputStream input = new FileInputStream(dfosFile);
            OutputStream output = getOutputStream();
            if (output != null) {
                IOUtils.copy(input, output);
                dfosFile.delete();
                dfosFile = null;
            }
        }
    }

    cachedContent = null;
}