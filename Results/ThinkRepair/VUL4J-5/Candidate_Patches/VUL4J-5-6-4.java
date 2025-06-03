private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
    throws IOException {
    String targetDirPath = targetDirectory.getCanonicalPath();

    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        File f = new File(targetDirectory, nextEntry.getName());
        File canonicalTargetDir = new File(targetDirPath).getCanonicalFile();
        File canonicalFile = f.getCanonicalFile();
        
        if (!canonicalFile.getPath().startsWith(canonicalTargetDir.getPath())) {
            throw new IOException("expanding " + nextEntry.getName()
                    + " would create file outside of " + targetDirectory);
        }
        
        if (nextEntry.isDirectory()) {
            if (!canonicalFile.isDirectory() && !canonicalFile.mkdirs()) {
                throw new IOException("failed to create directory " + canonicalFile);
            }
        } else {
            File parent = canonicalFile.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("failed to create directory " + parent);
            }
            try (OutputStream o = Files.newOutputStream(canonicalFile.toPath())) {
                writer.writeEntryDataTo(nextEntry, o);
            }
        }
        
        nextEntry = supplier.getNextReadableEntry();
    }
}