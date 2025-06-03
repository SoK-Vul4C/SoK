private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
    throws IOException {
    String targetDirPath = targetDirectory.getAbsoluteFile().getCanonicalPath();

    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        File f = new File(targetDirectory, nextEntry.getName());
        File canonicalf = f.getCanonicalFile();
        if (!canonicalf.getAbsolutePath().startsWith(targetDirPath)) {
            throw new IOException("expanding " + nextEntry.getName()
                    + " would create file outside of " + targetDirectory);
        }
        if (nextEntry.isDirectory()) {
            if (!canonicalf.isDirectory() && !canonicalf.mkdirs()) {
                throw new IOException("failed to create directory " + canonicalf);
            }
        } else {
            File parent = canonicalf.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("failed to create directory " + parent);
            }
            try (OutputStream o = Files.newOutputStream(canonicalf.toPath())) {
                writer.writeEntryDataTo(nextEntry, o);
            }
        }
        nextEntry = supplier.getNextReadableEntry();
    }
}