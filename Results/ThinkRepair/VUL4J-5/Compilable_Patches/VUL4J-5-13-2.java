private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
    throws IOException {
    if (!targetDirectory.isDirectory()) {
        throw new IllegalArgumentException("Target directory is not a valid directory");
    }

    String targetDirPath = targetDirectory.getCanonicalPath();
    String canonicalTargetDirPath = targetDirectory.isDirectory() ? targetDirPath : targetDirPath + File.separator;
    
    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        String entryName = nextEntry.getName();
        if (entryName.contains("..")) {
            throw new IOException("Invalid entry name contains '..': " + entryName);
        }
        
        File f = new File(targetDirectory, entryName);
        String canonicalFilePath = f.getCanonicalPath();
        
        if (!canonicalFilePath.startsWith(canonicalTargetDirPath)) {
            throw new IOException("Expanding " + entryName + " would create a file outside of " + targetDirectory);
        }
        
        if (nextEntry.isDirectory()) {
            if (!f.isDirectory() && !f.mkdirs()) {
                throw new IOException("Failed to create directory: " + f);
            }
        } else {
            File parent = f.getParentFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("Failed to create directory: " + parent);
            }
            
            try (OutputStream o = Files.newOutputStream(f.toPath())) {
                writer.writeEntryDataTo(nextEntry, o);
            }
        }
        nextEntry = supplier.getNextReadableEntry();
    }
}