private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = getOutputStream();

    if (output == null) {
        // Output stream is null, create a new one
        setOutputStream(new ByteArrayOutputStream());
        output = getOutputStream();
    }
    
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

    output.close();

    cachedContent = null;
}