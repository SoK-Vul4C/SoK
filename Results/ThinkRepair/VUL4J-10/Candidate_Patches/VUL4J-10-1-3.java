private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = getOutputStream();
    
    if (cachedContent != null) {
        if (output != null) {
            output.write(cachedContent);
            output.close();
            cachedContent = null;
        } else {
            throw new IOException("Output stream is null while writing cached content");
        }
    } else {
        if (dfosFile != null) {
            InputStream input = new FileInputStream(dfosFile);
            if (output != null) {
                IOUtils.copy(input, output);
                dfosFile.delete();
                dfosFile = null;
                output.close();
            } else {
                throw new IOException("Output stream is null while copying content from file");
            }
        } else {
            throw new IOException("Both cached content and file are null");
        }
    }
}