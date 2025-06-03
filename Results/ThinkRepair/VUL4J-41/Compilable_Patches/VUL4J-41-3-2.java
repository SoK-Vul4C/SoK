protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                            final String entryName, final Date entryDate, final boolean isDirectory,
                            final Integer mode, String symlinkDestination)
    throws IOException, ArchiverException
{
    // Hmm. Symlinks re-evaluate back to the original file here. Unsure if this is a good thing...
    final File f = FileUtils.resolveFile(dir, entryName);

    try
    {
        if (!isOverwrite() && f.exists())
        {
            long existingLastModified = f.lastModified();
            if (existingLastModified >= entryDate.getTime())
            {
                return; // File already exists and is up to date, no need to update
            }
        }

        // create intermediary directories - sometimes zip doesn't add them
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
            }
            finally
            {
                IOUtil.close(out);
            }
        }

        f.setLastModified(entryDate.getTime());

        if (!isIgnorePermissions() && mode != null && !isDirectory)
        {
            ArchiveEntryUtils.chmod(f, mode);
        }
    }
    catch (IOException ex)
    {
        getLogger().warn("Unable to expand to file " + f.getPath());
    }
}