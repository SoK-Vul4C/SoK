private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) {
    try {
        String targetDirCanonicalPath = targetDirectory.getCanonicalPath();
        
        ArchiveEntry nextEntry = supplier.getNextReadableEntry();
        
        while (nextEntry != null) {
            String entryName = nextEntry.getName();
            String canonicalFilePath = new File(targetDirectory, entryName).getCanonicalPath();
            
            if (!canonicalFilePath.startsWith(targetDirCanonicalPath + File.separator)) {
                throw new IOException("expanding " + entryName +
                                      " would create file outside of " + targetDirectory);
            }

            File f = new File(canonicalFilePath);
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
    } catch (IOException e) {
        // Handle the exception here or re-throw a more specific exception if needed
        e.printStackTrace();
    }
}