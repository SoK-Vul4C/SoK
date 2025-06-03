private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
    throws IOException {
    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        File f = new File(targetDirectory, nextEntry.getName()).getCanonicalFile();
        File parent = f.getParentFile();

        if (parent == null || !parent.getCanonicalPath().equals(targetDirectory.getCanonicalPath())) {
            throw new IOException("expanding " + nextEntry.getName()
                + " would create file outside of " + targetDirectory);
        }

        if (nextEntry.isDirectory()) {
            if (!f.isDirectory() && !f.mkdirs()) {
                throw new IOException("failed to create directory " + f);
            }
        } else {
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("failed to create directory " + parent);
            }
            try (OutputStream o = Files.newOutputStream(f.toPath())) {
                writer.writeEntryDataTo(nextEntry, o);
            }
        }
        nextEntry = supplier.getNextReadableEntry();
    }
}