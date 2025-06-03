private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = getOutputStream(); // Buggy Line
    if (output != null) {
        try {
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
            // Handle any IO exceptions
            e.printStackTrace();
            throw e;
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                // Handle closing exception
                e.printStackTrace();
            }
        }
    }

    cachedContent = null;
}