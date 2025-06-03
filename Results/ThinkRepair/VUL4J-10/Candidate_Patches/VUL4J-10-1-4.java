private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (cachedContent != null) {
        OutputStream output = getOutputStream();
        if (output != null) {
            try {
                output.write(cachedContent);
                output.close();
            } catch (IOException e) {
                throw new IOException("Error writing cached content to output stream", e);
            }
        } else {
            throw new IOException("Output stream is null");
        }
        cachedContent = null;
    } else if (dfosFile != null) {
        FileInputStream input = new FileInputStream(dfosFile);
        OutputStream output = getOutputStream();
        if (output != null) {
            try {
                IOUtils.copy(input, output);
                output.close();
                dfosFile.delete();
                dfosFile = null;
            } catch (IOException e) {
                throw new IOException("Error copying content from file to output stream", e);
            } finally {
                IOUtils.closeQuietly(input);
            }
        } else {
            throw new IOException("Output stream is null");
        }
    } else {
        throw new IOException("Both cached content and file are null");
    }
}