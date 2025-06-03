protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {
    final File f = FileUtils.resolveFile(dir, entryName);

    try {
        if (!isOverwrite() && f.exists() && (f.lastModified() >= entryDate.getTime())) {
            return;
        }

        // Create intermediary directories if required
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
        getLogger().warn("Unable to expand to file " + f.getPath());
        throw new ArchiverException("Failed to expand file '" + srcF.getName() + "' to directory '" + dir.getPath() + "'", ex);
    } catch (SecurityException ex) {
        getLogger().warn("Security Exception while expanding file " + f.getPath());
        throw new ArchiverException("Security Exception while expanding file '" + srcF.getName() + "' to directory '" + dir.getPath() + "'", ex);
    }
}