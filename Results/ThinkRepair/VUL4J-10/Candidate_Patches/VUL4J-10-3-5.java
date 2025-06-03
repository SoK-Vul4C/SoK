private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    OutputStream output = getOutputStream();

    ByteArrayOutputStream newOutput = null;

    if (output == null) {
        newOutput = new ByteArrayOutputStream();
        output = newOutput;
    }

    try {
        if (cachedContent != null) {
            output.write(cachedContent);
        } else {
            FileInputStream input = null;
            try {
                input = new FileInputStream(dfosFile);
                IOUtils.copy(input, output);
                dfosFile.delete();
                dfosFile = null;
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        }
    } finally {
        if (newOutput != null) {
            newOutput.close();
            setOutputStream(newOutput);
        } else {
            output.close();
        }
    }

    cachedContent = null;
}