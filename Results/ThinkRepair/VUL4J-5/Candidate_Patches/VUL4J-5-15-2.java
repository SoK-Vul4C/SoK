import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory)
        throws IOException {
    if (targetDirectory == null) {
        throw new IllegalArgumentException("Target directory is null");
    }

    String targetDirPath = targetDirectory.getCanonicalPath();

    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        Path path = Paths.get(targetDirectory.getPath(), nextEntry.getName());
        Path resolvedPath = path.normalize().toAbsolutePath().toRealPath();
        if (!resolvedPath.startsWith(targetDirPath)) {
            throw new IOException("expanding " + nextEntry.getName() +
                    " would create a file outside of " + targetDirectory);
        }

        if (nextEntry.isDirectory()) {
            File directory = resolvedPath.toFile();
            if (!directory.isDirectory() && !directory.mkdirs()) {
                throw new IOException("failed to create directory " + directory);
            }
        } else {
            File parent = resolvedPath.getParent().toFile();
            if (!parent.isDirectory() && !parent.mkdirs()) {
                throw new IOException("failed to create directory " + parent);
            }
            try (OutputStream o = Files.newOutputStream(resolvedPath)) {
                writer.writeEntryDataTo(nextEntry, o);
            }
        }
        nextEntry = supplier.getNextReadableEntry();
    }
}