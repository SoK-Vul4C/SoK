private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024]; // Buffer to read data from ObjectInputStream

    // Read data from the ObjectInputStream into ByteArrayOutputStream
    int bytesRead;
    while ((bytesRead = in.read(buffer)) != -1) {
        byteOutput.write(buffer, 0, bytesRead);
    }

    // Write the data to the OutputStream obtained from getOutputStream()
    OutputStream output = getOutputStream();
    output.write(byteOutput.toByteArray());

    // Clean up resources
    output.close();
    byteOutput.close(); 

    cachedContent = null;
}