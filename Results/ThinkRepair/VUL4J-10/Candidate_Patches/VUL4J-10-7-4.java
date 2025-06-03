private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (cachedContent != null) {
        try (OutputStream output = getOutputStream()) {
            if (output != null) {
                output.write(cachedContent);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException
        }
    } else {
        FileInputStream input = null;
        try {
            input = new FileInputStream(dfosFile);
            ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
            IOUtils.copy(input, outputContent);
            byte[] fileContent = outputContent.toByteArray();
            try (OutputStream output = getOutputStream()) {
                if (output != null) {
                    output.write(fileContent);
                }
                dfosFile.delete();
                dfosFile = null;
            } catch (IOException e) {
                e.printStackTrace(); // Handle IOException
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle IOException
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    cachedContent = null;
}