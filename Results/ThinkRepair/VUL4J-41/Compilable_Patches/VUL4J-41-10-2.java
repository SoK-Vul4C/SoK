protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
    throws IOException, ArchiverException
{
    final File f = FileUtils.resolveFile(dir, entryName);

    boolean fileProcessed = false;

    if (!isOverwrite() && f.exists() && (f.lastModified() >= entryDate.getTime()))
    {
        return;
    }

    try
    {
        // create intermediary directories - sometimes ZIP archives don't add them
        final File dirF = f.getParentFile();
        if (dirF != null)
        {
            dirF.mkdirs();
        }

        if (!StringUtils.isEmpty(symlinkDestination))
        {
            SymlinkUtils.createSymbolicLink(f, new File(symlinkDestination));
        }
        else if (isDirectory)
        {
            f.mkdirs();
        }
        else
        {
            OutputStream out = null;
            try
            {
                out = new FileOutputStream(f);
                IOUtil.copy(compressedInputStream, out);
                fileProcessed = true;
            }
            finally
            {
                IOUtil.close(out);
            }
        }

        if (fileProcessed) {
            f.setLastModified(entryDate.getTime());

            if (!isIgnorePermissions() && mode != null && !isDirectory)
            {
                ArchiveEntryUtils.chmod(f, mode);
            }
        }
    }
    catch (FileNotFoundException ex)
    {
        getLogger().warn("Unable to expand to file " + f.getPath() + ": " + ex.getMessage());
    }
}