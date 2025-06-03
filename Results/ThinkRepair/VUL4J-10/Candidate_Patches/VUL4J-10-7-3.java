private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (cachedContent != null) {
        OutputStream output = getOutputStream(); // Get the output stream
        try {
            if (output != null) {
                output.write(cachedContent);
                output.close(); // Close the output stream
            }
        } catch (IOException e) {
            // Handle any IO exceptions
            e.printStackTrace(); // Or log the exception
        }
    } else {
        FileInputStream input = null;
        try {
            input = new FileInputStream(dfosFile); // Read from file
            ByteArrayOutputStream content = new ByteArrayOutputStream();
            IOUtils.copy(input, content); // Copy data
            byte[] fileContent = content.toByteArray();
            OutputStream output = getOutputStream(); // Get the output stream
            if (output != null) {
                output.write(fileContent);
                output.close(); // Close the output stream
                dfosFile.delete(); // Delete file
                dfosFile = null; // Clear the file reference
            }
        } catch (IOException e) {
            // Handle any IO exceptions
            e.printStackTrace(); // Or log the exception
        } finally {
            // Close input stream if opened
            if (input != null) {
                input.close();
            }
        }
    }

    cachedContent = null; // Reset cached content
}