private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) {
    try {
        String targetDirPath = targetDirectory.getCanonicalPath();
        ArchiveEntry nextEntry = supplier.getNextReadableEntry();
        
        if (!targetDirPath.endsWith(File.separator)) {
            targetDirPath += File.separator;
        }

        while (nextEntry != null) {
            String entryName = nextEntry.getName();
            String entryPath = targetDirPath + entryName;

            File f = new File(entryPath);
            String canonicalPath = f.getCanonicalPath();

            if (!canonicalPath.startsWith(targetDirPath)) {
                throw new IOException("expanding " + entryName +
                                      " would create file outside of " + targetDirectory);
            }

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