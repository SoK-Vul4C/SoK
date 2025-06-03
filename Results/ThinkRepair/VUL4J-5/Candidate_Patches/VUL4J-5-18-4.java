import java.io.IOException;
import java.nio.file.*;
import java.io.OutputStream;

private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) throws IOException {
    Path targetDirPath = targetDirectory.toPath().toRealPath();

    ArchiveEntry nextEntry = supplier.getNextReadableEntry();
    while (nextEntry != null) {
        Path entryPath = targetDirectory.toPath().resolve(nextEntry.getName()).toRealPath(LinkOption.NOFOLLOW_LINKS);

        if (!entryPath.startsWith(targetDirPath)) {
            throw new IOException("expanding " + nextEntry.getName() +
                " would create a file outside of " + targetDirectory);
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