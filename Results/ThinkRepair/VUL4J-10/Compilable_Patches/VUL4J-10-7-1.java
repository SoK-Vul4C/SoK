private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = getOutputStream(); // Get output stream

    if (output != null) {
        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            FileInputStream input = new FileInputStream(dfosFile); // Read from file
            IOUtils.copy(input, output); // Copy data
            dfosFile.delete(); // Delete file
            dfosFile = null; // Clear the file reference
        }
        output.close(); // Close the output stream
    }

    cachedContent = null; // Reset cached content
}