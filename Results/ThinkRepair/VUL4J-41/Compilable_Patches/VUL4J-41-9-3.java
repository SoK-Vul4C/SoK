protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
        
    final File destFile = new File(dir, entryName).getCanonicalFile();
    final File canonicalDir = dir.getCanonicalFile();

    if (!destFile.toPath().normalize().startsWith(canonicalDir.toPath())) {
        throw new ArchiverException("Entry is outside the target directory: " + entryName);
    }

    if (!isOverwrite() && destFile.exists() && (destFile.lastModified() >= entryDate.getTime())) {
        return;  // Skip extraction if file already exists and is up to date
    }

    destFile.getParentFile().mkdirs();  // Create parent directories if they don't exist

    if (isDirectory) {
        destFile.mkdirs();  // Create a directory if the entry represents a directory
    } else if (!StringUtils.isEmpty(symlinkDestination)) {
        SymlinkUtils.createSymbolicLink(destFile, new File(symlinkDestination));  // Create a symbolic link
    } else {
        try (OutputStream out = new FileOutputStream(destFile)) {
            IOUtil.copy(compressedInputStream, out);  // Extract the file content
        }

        destFile.setLastModified(entryDate.getTime());  // Set the last modified timestamp

        if (!isIgnorePermissions() && mode != null) {
            ArchiveEntryUtils.chmod(destFile, mode);  // Set file permissions if specified
        }
    }
}