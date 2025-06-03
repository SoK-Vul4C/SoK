protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    final File f = FileUtils.resolveFile(dir, entryName);

    // Check if the resolved file is outside the destination directory
    if (!FileUtils.isLeadingPath(dir, f)) {
        throw new ArchiverException("Entry is outside the destination directory: " + entryName);
    }

    // Check if the resolved file is a symbolic link
    if (!FileUtils.isSymLink(f)) {
        throw new ArchiverException("Symbolic link detected: " + entryName);
    }

    if (!isDirectory) {
        OutputStream out = null;
        try {
            final File destDir = f.getParentFile();
            if (destDir != null) {
                destDir.mkdirs();
            }

            out = new FileOutputStream(f);
            IOUtil.copy(compressedInputStream, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }

        f.setLastModified(entryDate.getTime());

        if (!isIgnorePermissions() && mode != null) {
            ArchiveEntryUtils.chmod(f, mode);
        }
    } else {
        f.mkdirs();
    }
}