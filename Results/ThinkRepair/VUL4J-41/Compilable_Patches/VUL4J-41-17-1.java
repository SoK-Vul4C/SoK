protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    // Hmm. Symlinks reevaluate back to the original file here. Unsure if this is a good thing...
    final File f = FileUtils.resolveFile(dir, entryName);

    if (!isOverwrite() && f.exists() && (f.lastModified() >= entryDate.getTime())) {
        return;
    }

    // create intermediary directories - sometimes zip don't add them
    final File dirF = f.getParentFile();
    if (dirF != null) {
        dirF.mkdirs();
    }

    try {
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
    } catch (final FileNotFoundException ex) {
        getLogger().warn("Unable to expand to file " + f.getPath());
    }
}