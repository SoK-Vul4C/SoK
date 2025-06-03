import org.apache.commons.io.IOUtils;

private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();

    if (dfosFile == null && cachedContent != null) {
        cachedContent = null;
        return;
    }
    
    if (dfosFile != null) {
        OutputStream output = getOutputStream();

        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            FileInputStream input = new FileInputStream(dfosFile);
            try {
                IOUtils.copy(input, output);
            } finally {
                input.close();
            }
            if (dfosFile.exists()) {
                dfosFile.delete();
            }
            dfosFile = null;
        }

        output.close();
    }
}