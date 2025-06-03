private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (getOutputStream() == null) {
        // If output stream is not available, handle the situation accordingly
        cachedContent = null;
        return;
    }

    OutputStream output = getOutputStream();
    try {
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
    } catch (IOException e) {
        // Rethrow the IOException to match the expected exception
        throw e;
    } finally {
        // Close the output stream in the finally block to ensure it always gets closed
        output.close();
    }

    cachedContent = null;
}