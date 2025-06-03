import java.nio.file.*;

private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
    throws IOException {
    String targetDirPath = targetDirectory.getCanonicalPath();

    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        Path f = new File(targetDirectory, nextEntry.getName()).toPath().toRealPath();
        String resolvedFPath = f.toString();
        
        if (!resolvedFPath.startsWith(targetDirPath + File.separator)) {
            throw new IOException("expanding " + nextEntry.getName()
                + " would create file outside of " + targetDirectory);
        }
        
        if (nextEntry.isDirectory()) {
            Files.createDirectories(f);
        } else {
            File parentFile = f.getParent().toFile();
            if (!parentFile.isDirectory()) {
                Files.createDirectories(parentFile.toPath());
            }
            try (OutputStream o = Files.newOutputStream(f)) {
                writer.writeEntryDataTo(nextEntry, o);
            }
        }
        
        nextEntry = supplier.getNextReadableEntry();
    }
}