private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (cachedContent != null) {
        OutputStream output = getOutputStream(); // Get output stream
        if (output != null) {
            output.write(cachedContent);
            output.close(); // Close the output stream
        }
    } else {
        FileInputStream input = new FileInputStream(dfosFile); // Read from file
        OutputStream output = getOutputStream(); // Get output stream
        if (output != null) {
            IOUtils.copy(input, output); // Copy data
            output.close(); // Close the output stream
        }
        dfosFile.delete(); // Delete file
        dfosFile = null; // Clear the file reference
    }
    
    cachedContent = null; // Reset cached content
}