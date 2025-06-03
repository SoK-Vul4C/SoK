private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) throws IOException {
    String targetDirPath = targetDirectory.getCanonicalPath();
    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        File f = new File(targetDirectory, nextEntry.getName());
        if (!f.getCanonicalPath().startsWith(targetDirPath)) {
            throw new IOException("Expanding " + nextEntry.getName() + " would create a file outside of " + targetDirectory);
        }
        if (nextEntry.isDirectory()) {
            if (!f.isDirectory() && !f.mkdirs()) {
                throw new IOException("Failed to create directory " + f);
            }
        } else {
            File parent = f.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("Failed to create directory " + parent);
            }
            try (OutputStream o = Files.newOutputStream(f.toPath())) {
                writer.writeEntryDataTo(nextEntry, o);
            }
        }
        nextEntry = supplier.getNextReadableEntry();
    }
}