protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                           final String entryName, final Date entryDate, final boolean isDirectory,
                           final Integer mode, String symlinkDestination)
        throws IOException, ArchiverException
{
    // Hmm. Symlinks re-evaluate back to the original file here. Unsure if this is a good thing...
    final File f = new File(dir, entryName);

    try
    {
        // Check if the canonical path of the parent directory of 'f' starts with the canonical path of 'dir'
        if (!f.getCanonicalFile().getParentFile().getCanonicalPath().startsWith(dir.getCanonicalPath()))
        {
            throw new ArchiverException("Entry '" + entryName + "' is outside the destination directory");
        }

        if (!isOverwrite() && f.exists() && (f.lastModified() >= entryDate.getTime()))
        {
            return;
        }

        // create intermediary directories - sometimes zip don't add them
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
            try (OutputStream out = new FileOutputStream(f))
            {
                IOUtil.copy(compressedInputStream, out);
            }
        }

        f.setLastModified(entryDate.getTime());

        if (!isIgnorePermissions() && mode != null && !isDirectory)
        {
            ArchiveEntryUtils.chmod(f, mode);
        }
    }
    catch (FileNotFoundException ex)
    {
        getLogger().warn("Unable to expand to file " + f.getPath());
    }
    catch (IOException ex)
    {
        // Handle IOException here
        getLogger().error("IOException occurred: " + ex.getMessage());
        throw ex;
    }
    catch (ArchiverException ex)
    {
        // Handle ArchiverException here
        getLogger().error("ArchiverException occurred: " + ex.getMessage());
        throw ex;
    }
}