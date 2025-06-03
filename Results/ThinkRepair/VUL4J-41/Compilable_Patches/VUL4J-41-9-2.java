protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException {

    // Resolve the destination file path
    final File f = new File(dir, entryName);
    
    if (!f.getCanonicalPath().startsWith(dir.getCanonicalPath())) {
        throw new ArchiverException("Extracted entry is outside the destination directory");
    }

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
        try (OutputStream out = new FileOutputStream(f)) {
            IOUtil.copy(compressedInputStream, out);
        }

        f.setLastModified(entryDate.getTime());

        if (!isIgnorePermissions() && mode != null && !isDirectory) {
            ArchiveEntryUtils.chmod(f, mode);
        }
    }
}