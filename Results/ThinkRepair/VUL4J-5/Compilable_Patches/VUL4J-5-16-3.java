private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
    throws IOException {
    String targetDirCanonicalPath = targetDirectory.getCanonicalPath();
    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        File file = new File(targetDirectory, nextEntry.getName());
        String fileCanonicalPath = file.getCanonicalPath();
        if (!fileCanonicalPath.startsWith(targetDirCanonicalPath + File.separator) && !fileCanonicalPath.equals(targetDirCanonicalPath)) {
            throw new IOException("expanding " + file.getName() +
                " would create file outside of " + targetDirectory);
        }
        if (nextEntry.isDirectory()) {
            if (!file.isDirectory() && !file.mkdirs()) {
                throw new IOException("failed to create directory " + file);
            }
        } else {
            File parent = file.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("failed to create directory " + parent);
            }
            try (OutputStream outputStream = Files.newOutputStream(file.toPath())) {
                writer.writeEntryDataTo(nextEntry, outputStream);
            }
        }
        nextEntry = supplier.getNextReadableEntry();
    }
}