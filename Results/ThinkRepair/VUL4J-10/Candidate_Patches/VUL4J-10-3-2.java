private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (getOutputStream() == null) {
        // Output stream is null, create a new one
        setOutputStream(new ByteArrayOutputStream());
    }

    OutputStream output = getOutputStream();

    try {
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
    } finally {
        output.close();
    }

    cachedContent = null;
}