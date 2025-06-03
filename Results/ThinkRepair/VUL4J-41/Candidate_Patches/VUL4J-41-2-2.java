protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    // Resolve the target file path
    final File f = FileUtils.resolveFile(dir, entryName);

    // Check if overwrite is needed
    if (!isOverwrite() && f.exists() && (f.lastModified() >= entryDate.getTime())) {
        return;
    }

    try {
        // Create intermediary directories if needed
        final File dirF = f.getParentFile();
        if (dirF != null) {
            dirF.mkdirs();
        }

        // Handle different cases based on whether it's a symbolic link, directory, or file
        if (!StringUtils.isEmpty(symlinkDestination)) {
            SymlinkUtils.createSymbolicLink(f, new File(symlinkDestination));
        } else if (isDirectory) {
            f.mkdirs();
        } else {
            OutputStream out = new FileOutputStream(f);
            try {
                IOUtil.copy(compressedInputStream, out);
            } finally {
                out.close();
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
        // Close the output stream if an exception occurs during file extraction
        getLogger().error("An error occurred during file extraction: " + e.getMessage());
        throw new ArchiverException("Error extracting file " + f.getPath(), e);
    }
}