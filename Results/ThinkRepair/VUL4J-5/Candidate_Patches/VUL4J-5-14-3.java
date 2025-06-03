private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) throws IOException {
    Path targetDirPath = targetDirectory.toPath().toRealPath();
    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        Path entryPath = targetDirPath.resolve(nextEntry.getName()).normalize();

        if (!entryPath.startsWith(targetDirPath)) {
            throw new IOException("Expanding " + nextEntry.getName() + " would create a file outside of " + targetDirectory);
        }

        Path parentDir = entryPath.getParent();
        if (parentDir != null && !Files.isDirectory(parentDir)) {
            Files.createDirectories(parentDir);
        }

        if (nextEntry.isDirectory()) {
            if (!Files.isDirectory(entryPath)) {
                Files.createDirectory(entryPath);
            }
        } else {
            try (OutputStream o = Files.newOutputStream(entryPath)) {
                writer.writeEntryDataTo(nextEntry, o);
            }
        }

        nextEntry = supplier.getNextReadableEntry();
    }
}