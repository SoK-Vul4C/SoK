import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) {
    try {
        String targetDirCanonicalPath = targetDirectory.getCanonicalPath();
        java.nio.file.Path targetDirPath = Paths.get(targetDirCanonicalPath).toAbsolutePath().normalize();

        ArchiveEntry nextEntry = supplier.getNextReadableEntry();
        while (nextEntry != null) {
            File f = new File(targetDirectory, nextEntry.getName());
            java.nio.file.Path entryPath = f.getCanonicalFile().toPath().toRealPath().normalize();

            if (!entryPath.startsWith(targetDirPath)) {
                throw new IOException("Expanding " + nextEntry.getName()
                        + " would create a file outside of " + targetDirectory);
            }

            if (nextEntry.isDirectory()) {
                if (!f.isDirectory() && !(f.mkdirs() || f.isDirectory())) {
                    throw new IOException("Failed to create directory " + f);
                }
            } else {
                File parent = f.getParentFile();
                if (parent != null && !parent.isDirectory() && !(parent.mkdirs() || parent.isDirectory())) {
                    throw new IOException("Failed to create directory " + parent);
                }
                try (OutputStream o = Files.newOutputStream(f.toPath())) {
                    writer.writeEntryDataTo(nextEntry, o);
                }
            }
            nextEntry = supplier.getNextReadableEntry();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}