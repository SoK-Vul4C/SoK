private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) throws IOException {
    Path targetDirPath = targetDirectory.toPath();
    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        Path entryPath = Paths.get(targetDirectory.getAbsolutePath(), nextEntry.getName());

        if (!entryPath.normalize().startsWith(targetDirPath.normalize())) {
            throw new IOException("Expanding " + nextEntry.getName() + " would create a file outside of " + targetDirectory);
        }

        if (nextEntry.isDirectory()) {
            Path newDir = Paths.get(targetDirPath.toString(), nextEntry.getName());
            if (!Files.isDirectory(newDir) && !Files.createDirectories(newDir)) {
                throw new IOException("Failed to create directory " + newDir);
            }
        } else {
            Path parentDir = entryPath.getParent();
            if (!Files.isDirectory(parentDir) && !Files.createDirectories(parentDir)) {
                throw new IOException("Failed to create directory " + parentDir);
            }
            try (OutputStream o = Files.newOutputStream(entryPath)) {
                writer.writeEntryDataTo(nextEntry, o);
            }
        }

        nextEntry = supplier.getNextReadableEntry();
    }
}