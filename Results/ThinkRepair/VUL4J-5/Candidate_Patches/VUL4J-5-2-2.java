import java.nio.file.Paths;

private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) throws IOException {
    String targetDirPath = targetDirectory.getCanonicalPath();
    Path targetDirCanonicalPath = Paths.get(targetDirPath).normalize().toAbsolutePath();

    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        File f = new File(targetDirectory, nextEntry.getName());
        Path entryCanonicalPath = f.getCanonicalFile().toPath();
        
        if (!entryCanonicalPath.startsWith(targetDirCanonicalPath)) {
            throw new IOException("expanding " + nextEntry.getName()
                    + " would create file outside of " + targetDirectory);
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