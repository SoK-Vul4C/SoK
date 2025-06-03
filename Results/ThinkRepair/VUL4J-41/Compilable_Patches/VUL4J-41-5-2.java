protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    // Resolve the target file path
    final File f = FileUtils.resolveFile(dir, entryName);

    try {
        if (!isOverwrite() && f.exists() && f.lastModified() >= entryDate.getTime()) {
            return;  // Skip extraction if the file exists and is up to date
        }

        // Create intermediary directories if necessary
        final File dirF = f.getParentFile();
        if (dirF != null && !dirF.exists()) {
            if (!dirF.mkdirs()) {
                throw new ArchiverException("Failed to create directory: " + dirF.getPath());
            }
        }

        if (!StringUtils.isEmpty(symlinkDestination)) {
            // Create a symbolic link
            SymlinkUtils.createSymbolicLink(f, new File(symlinkDestination));
        } else if (isDirectory) {
            // Create a directory
            if (!f.exists() && !f.mkdirs()) {
                throw new ArchiverException("Failed to create directory: " + f.getPath());
            }
        } else {
            // Extract the file content
            try (OutputStream out = new FileOutputStream(f)) {
                IOUtil.copy(compressedInputStream, out);
            }
        }

        // Set the last modified timestamp
        f.setLastModified(entryDate.getTime());

        // Set file permissions if needed
        if (!isIgnorePermissions() && mode != null && !isDirectory) {
            ArchiveEntryUtils.chmod(f, mode);
        }
    } catch (FileNotFoundException ex) {
        throw new ArchiverException("Failed to extract file: " + f.getPath(), ex);
    }
}