protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    // Resolve the target file path
    final File f = FileUtils.resolveFile(dir, entryName);

    // Check if the resolved file is still within the destination directory
    if (!FileUtils.isLeadingPath(dir, f)) {
        throw new ArchiverException("Entry is outside the destination directory: " + f);
    }

    if (!isOverwrite() && f.exists() && (f.lastModified() >= entryDate.getTime())) {
        return;
    }

    // Create intermediary directories if needed
    final File dirF = f.getParentFile();
    if (dirF != null) {
        dirF.mkdirs();
    }

    try {
        // Handle different cases based on whether it's a symbolic link, directory, or file
        if (!StringUtils.isEmpty(symlinkDestination)) {
            SymlinkUtils.createSymbolicLink(f, new File(symlinkDestination));
        } else if (isDirectory) {
            f.mkdirs();
        } else {
            try (OutputStream out = new FileOutputStream(f)) {
                IOUtil.copy(compressedInputStream, out);
            }
        }

        f.setLastModified(entryDate.getTime());

        // Apply permissions if needed
        if (!isIgnorePermissions() && mode != null && !isDirectory) {
            ArchiveEntryUtils.chmod(f, mode);
        }
    } catch (FileNotFoundException ex) {
        getLogger().warn("Unable to expand to file " + f.getPath());
    } catch (IOException e) {
        getLogger().error("An error occurred during file extraction: " + e.getMessage());
        throw new ArchiverException("Error extracting file " + f.getPath(), e);
    }
}