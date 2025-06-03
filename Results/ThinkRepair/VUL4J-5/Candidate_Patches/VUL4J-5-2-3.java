import java.nio.file.FileSystems;
import java.nio.file.Path;

private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, File targetDirectory) {
    try {
        String targetDirCanonicalPath = targetDirectory.getCanonicalPath();
        Path targetDirPath = FileSystems.getDefault().getPath(targetDirCanonicalPath).toAbsolutePath().normalize();

        ArchiveEntry nextEntry = supplier.getNextReadableEntry();
        while (nextEntry != null) {
            File f = new File(targetDirectory, nextEntry.getName());
            Path entryPath = f.toPath().toRealPath().normalize();

            if (!entryPath.startsWith(targetDirPath)) {
                throw new IOException("Expanding " + nextEntry.getName()
                        + " would create a file outside of " + targetDirectory);
            }

            if (nextEntry.isDirectory()) {
                if ((!f.isDirectory() && !f.mkdirs()) || !f.exists()) {
                    throw new IOException("Failed to create directory " + f);
                }
            } else {
                File parent = f.getParentFile();
                if ((parent != null && !parent.isDirectory() && !parent.mkdirs()) || !parent.exists()) {
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