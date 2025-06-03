private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) throws IOException {
    // Get the canonical path of the target directory
    String targetDirPath = targetDirectory.getCanonicalPath();
    
    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    
    while (nextEntry != null) {
        File f = new File(targetDirectory, nextEntry.getName());
        
        // Get the canonical path of the file to check if it would be created outside the target directory
        String filePathCanonical = f.getCanonicalPath();
        
        // Ensure that the canonical path of the file starts with the canonical path of the target directory
        if (!filePathCanonical.startsWith(targetDirPath + File.separator)) {
            throw new IOException("expanding " + nextEntry.getName() +
                    " would create file outside of " + targetDirectory);
        }

        // Handle directory or file creation
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
        
        // Get the next entry
        nextEntry = supplier.getNextReadableEntry();
    }
}