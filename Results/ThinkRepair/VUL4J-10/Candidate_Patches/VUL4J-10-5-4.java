private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = null;
    try {
        output = getOutputStream();
        if (output != null) {
            if (cachedContent != null) {
                output.write(cachedContent);
            } else {
                try (FileInputStream input = new FileInputStream(dfosFile)) {
                    IOUtils.copy(input, output);
                } 
                dfosFile.delete();
                dfosFile = null;
            }
        }
    } finally {
        if (output != null) {
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