private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = null;
    FileInputStream input = null;
    
    try {
        output = getOutputStream();

        if (cachedContent != null) {
            if (output != null) {
                output.write(cachedContent);
            }
        } else {
            if (output != null) {
                input = new FileInputStream(dfosFile);
                IOUtils.copy(input, output);
                dfosFile.delete();
                dfosFile = null;
            }
        }
    } finally {
        if (output != null) {
            output.close();
        }
        if (input != null) {
            input.close();
        }
    }

    cachedContent = null;
}