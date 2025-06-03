import org.apache.commons.io.IOUtils;

private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();

    if (dfosFile == null) {
        cachedContent = null;
        return;
    }

    try (OutputStream output = getOutputStream()) {
        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            try (FileInputStream input = new FileInputStream(dfosFile)) {
                IOUtils.copy(input, output);
            }
            if (dfosFile.exists()) {
                dfosFile.delete();
            }
            dfosFile = null;
        }
    }
}