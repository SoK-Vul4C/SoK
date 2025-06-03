private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (getOutputStream() != null) {
        OutputStream output = getOutputStream();

        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            FileInputStream input = new FileInputStream(dfosFile);
            IOUtils.copy(input, output);
            dfosFile.delete();
            dfosFile = null;
        }

        output.close();
    }

    cachedContent = null;
}