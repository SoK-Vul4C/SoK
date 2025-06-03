import java.nio.file.*;

private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
        throws IOException {
    String targetDirPath = targetDirectory.getCanonicalPath();

    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        String entryName = nextEntry.getName();
        Path entryPath = Paths.get(targetDirectory.getPath(), entryName);
        String entryCanonicalPath = entryPath.toFile().getCanonicalPath();

        if (!entryCanonicalPath.startsWith(targetDirPath)) {
            throw new IOException("expanding " + entryName + " would create a file outside of " + targetDirectory);
        }

        if (nextEntry.isDirectory()) {
            Files.createDirectories(entryPath);
        } else {
            Path parentDir = entryPath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
                try (OutputStream outputStream = Files.newOutputStream(entryPath)) {
                    writer.writeEntryDataTo(nextEntry, outputStream);
                }
            }
        }

        nextEntry = supplier.getNextReadableEntry();
    }
}