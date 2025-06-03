private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    transient OutputStream output = null;

    try {
        output = getOutputStream();

        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            try (FileInputStream input = new FileInputStream(dfosFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            if (!dfosFile.delete()) {
                throw new IOException("Failed to delete temporary file: " + dfosFile.getAbsolutePath());
            }
        }

        cachedContent = null;
    } finally {
        if (output != null) {
            output.close();
        }
    }
}