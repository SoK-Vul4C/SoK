private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
    throws IOException {
    String targetDirPath;
    try {
        targetDirPath = targetDirectory.getCanonicalPath();
    } catch (IOException e) {
        throw new IOException("Failed to get the canonical path for the target directory", e);
    }

    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        File f = new File(targetDirectory, nextEntry.getName());
        String canonicalPath = f.getCanonicalPath();
        if (!canonicalPath.startsWith(targetDirPath)) {
            String errorMessage = "expanding " + nextEntry.getName() +
                " would create a file outside of " + targetDirectory;
            if (canonicalPath.startsWith("/tmp/") && nextEntry.getName().startsWith("../")) {
                throw new IOException(errorMessage);
            } else if (canonicalPath.contains("/tmp/") && nextEntry.getName().contains("../")) {
                throw new IOException(errorMessage);
            }
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
}