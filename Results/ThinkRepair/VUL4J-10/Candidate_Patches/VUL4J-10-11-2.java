private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    if (cachedContent != null) {
        byteOutput.write(cachedContent);
    } else {
        try (FileInputStream fileInput = new FileInputStream(dfosFile)) {
            IOUtils.copy(fileInput, byteOutput);
            dfosFile.delete();
            dfosFile = null;
        }
    }

    ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
    OutputStream output = getOutputStream();

    IOUtils.copy(byteInput, output);

    output.close();
    byteInput.close();
    byteOutput.close();

    cachedContent = null;
}