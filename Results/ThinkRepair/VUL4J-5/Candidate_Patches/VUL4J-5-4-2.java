private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
    throws IOException {
    if (targetDirectory == null) {
        throw new IllegalArgumentException("Target directory cannot be null");
    }

    String targetDirCanonicalPath = targetDirectory.getCanonicalPath();

    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        String nextEntryName = nextEntry.getName();
        String canonicalEntryPath = new File(targetDirectory, nextEntryName).getCanonicalPath();

        if (!canonicalEntryPath.startsWith(targetDirCanonicalPath + File.separator)) {
            throw new IOException("expanding " + nextEntryName +
                " would create file outside of " + targetDirCanonicalPath);
        }

        File f = new File(targetDirectory, nextEntryName);
        if (nextEntry.isDirectory()) {
            if (!f.isDirectory() && !f.mkdirs()) {
                throw new IOException("failed to create directory " + f);
            }
        } else {
            File parent = f.getParentFile();
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