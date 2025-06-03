protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    // Resolve the target file path
    final File f = FileUtils.resolveFile(dir, entryName);

    // Check if the resolved file is outside the destination directory
    if (!isChild(dir.getCanonicalPath(), f.getCanonicalPath())) {
        throw new ArchiverException("Entry '" + entryName + "' is outside the destination directory");
    }

    try {
        // Rest of the extraction process remains the same
        if (!isOverwrite() && f.exists() && (f.lastModified() >= entryDate.getTime())) {
            return;
        }

        // create intermediary directories - sometimes zip doesn't add them
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
                out.close();
                out = null;
            } finally {
                IOUtil.close(out);
            }
        }

        f.setLastModified(entryDate.getTime());

        if (!isIgnorePermissions() && mode != null && !isDirectory) {
            ArchiveEntryUtils.chmod(f, mode);
        }
    } catch (final FileNotFoundException ex) {
        throw new ArchiverException("Unable to expand to file " + f.getPath(), ex);
    }
}

private boolean isChild(String parent, String child) {
    return child.startsWith(parent + File.separator);
}