protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    final File f = FileUtils.resolveFile(dir, entryName);

    // Verify that the resolved file is still under the destination directory
    if (!FileUtils.isLeadingPath(dir, f)) {
        throw new ArchiverException("Entry is outside the destination directory: " + entryName);
    }

    try {
        if (!isOverwrite() && f.exists() && (f.lastModified() >= entryDate.getTime())) {
            return;
        }

        final File dirF = f.getParentFile();
        if (dirF != null) {
            dirF.mkdirs();
        }

        if (!StringUtils.isEmpty(symlinkDestination)) {
            SymlinkUtils.createSymbolicLink(f, new File(symlinkDestination));
        } else if (isDirectory) {
            f.mkdirs();
        } else {
            OutputStream out = null;
            try {
                out = new FileOutputStream(f);
                IOUtil.copy(compressedInputStream, out);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }

        f.setLastModified(entryDate.getTime());

        if (!isIgnorePermissions() && mode != null && !isDirectory) {
            ArchiveEntryUtils.chmod(f, mode);
        }
    } catch (FileNotFoundException ex) {
        // Handle or rethrow the exception as needed
        throw new ArchiverException("Unable to expand to file " + f.getPath(), ex);
    }
}