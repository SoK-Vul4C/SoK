private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // read values
    in.defaultReadObject();

    if (cachedContent != null) {
        // Do something with cachedContent
        cachedContent = null;
    } else {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dfosFile);
            IOUtils.copy(in, fos);
            dfosFile.delete();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        dfosFile = null;
    }
}