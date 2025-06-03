protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    // Resolve the target file path
    final File f = FileUtils.resolveFile(dir, entryName);

    // Ensure the extracted file is within the destination directory
    if (!FileUtils.isLeadingPath(srcF, f)) {
        throw new ArchiverException("Entry '" + entryName + "' is outside the target directory");
    }

    // Other extraction logic remains the same...
    try {
        // Existing extraction logic...
    } catch (IOException ex) {
        getLogger().warn("An error occurred while extracting the file " + f.getPath() + ": " + ex.getMessage());
        throw ex; // Rethrow the IOException for higher-level handling
    }
}