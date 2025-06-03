protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    try {
        // Hmm. Symlinks re-evaluate back to the original file here. Unsure if this is a good thing...
        final File f = FileUtils.resolveFile(dir, entryName);

        if (!f.getCanonicalPath().startsWith(dir.getCanonicalPath())) {
            throw new ArchiverException("Entry is outside the target directory: " + f.getPath());
        }

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
            try (OutputStream out = new FileOutputStream(f)) {
                IOUtil.copy(compressedInputStream, out);
            }

            f.setLastModified(entryDate.getTime());

            if (!isIgnorePermissions() && mode != null && !isDirectory) {
                ArchiveEntryUtils.chmod(f, mode);
            }
        }
    } catch (final FileNotFoundException ex) {
        getLogger().warn("Unable to expand to file " + ex.getMessage());
    }
}