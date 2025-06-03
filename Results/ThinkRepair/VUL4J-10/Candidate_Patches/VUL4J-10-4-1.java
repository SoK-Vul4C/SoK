private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (cachedContent != null) {
        OutputStream output = getOutputStream(); // Move inside the if condition
        output.write(cachedContent);
        output.close();
    } else {
        OutputStream output = getOutputStream(); // Move inside the else condition
        FileInputStream input = new FileInputStream(dfosFile);
        IOUtils.copy(input, output);
        input.close();
        dfosFile.delete();
        dfosFile = null;
        output.close();
    }

    cachedContent = null;
}