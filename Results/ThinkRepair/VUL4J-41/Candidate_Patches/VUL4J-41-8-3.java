protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
    throws IOException, ArchiverException {
    final File resolvedFile = new File(dir, entryName).getCanonicalFile();
    final File targetDir = dir.getCanonicalFile();

    if (!FileUtils.isAncestor(targetDir, resolvedFile)) {
        throw new ArchiverException("Entry path is outside of the target directory: " + entryName);
    }

    try {
        if (!isOverwrite() && resolvedFile.exists() && (resolvedFile.lastModified() >= entryDate.getTime())) {
            return;
        }

        // create intermediary directories - sometimes zip doesn't add them
        File parentDir = resolvedFile.getParentFile();
        if (parentDir != null) {
            parentDir.mkdirs();
        }

        if (!StringUtils.isEmpty(symlinkDestination)) {
            SymlinkUtils.createSymbolicLink(resolvedFile, new File(symlinkDestination));
        } else if (isDirectory) {
            resolvedFile.mkdirs();
        } else {
            try (OutputStream out = new FileOutputStream(resolvedFile)) {
                IOUtil.copy(compressedInputStream, out);
            }
        }

        resolvedFile.setLastModified(entryDate.getTime());

        if (!isIgnorePermissions() && mode != null && !isDirectory) {
            ArchiveEntryUtils.chmod(resolvedFile, mode);
        }
    } catch (FileNotFoundException ex) {
        getLogger().warn("Unable to expand to file " + resolvedFile.getPath());
        throw ex;
    }
}