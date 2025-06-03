private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = getOutputStream(); // Buggy Line
    if (cachedContent != null) {
        if (output != null) {
            try {
                output.write(cachedContent);
            } finally {
                output.close();
            }
        }
    } else {
        FileInputStream input = null;
        try {
            if (output != null) {
                input = new FileInputStream(dfosFile);
                IOUtils.copy(input, output);
                dfosFile.delete();
                dfosFile = null;
            }
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    cachedContent = null;
}