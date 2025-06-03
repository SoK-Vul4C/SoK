import org.apache.commons.io.IOUtils;

private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();
    
    try (OutputStream output = getOutputStream()) {
        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            try (FileInputStream input = new FileInputStream(dfosFile)) {
                IOUtils.copy(input, output);
            }
            dfosFile.delete();
            dfosFile = null;
        }
    } catch (IOException e) {
        // Handle any IO exception
        e.printStackTrace(); // Or log the exception
        throw e; // Rethrow the exception if needed
    }

    cachedContent = null;
}