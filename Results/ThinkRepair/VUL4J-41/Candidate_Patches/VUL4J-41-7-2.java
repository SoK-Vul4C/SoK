protected void extractFile( final File srcF, final File dir, final InputStream compressedInputStream,
                            final String entryName, final Date entryDate, final boolean isDirectory,
                            final Integer mode, String symlinkDestination )
    throws IOException, ArchiverException
{
    // Resolve the destination file
    final File f = FileUtils.resolveFile( dir, entryName );

    try 
    {
        // Check if overwrite is disabled and file already exists
        if ( !isOverwrite() && f.exists() && ( f.lastModified() >= entryDate.getTime() ) )
        {
            return;
        }

        // Create intermediary directories if necessary
        final File dirF = f.getParentFile();
        if ( dirF != null )
        {
            dirF.mkdirs();
        }

        // Handle symbolic links, directories, and regular files
        if ( !StringUtils.isEmpty( symlinkDestination ) )
        {
            SymlinkUtils.createSymbolicLink( f, new File( symlinkDestination ) );
        }
        else if ( isDirectory )
        {
            f.mkdirs();
        }
        else
        {
            // Extract the file
            OutputStream out = null;
            try
            {
                out = new FileOutputStream( f );
                IOUtil.copy( compressedInputStream, out );
            }
            finally
            {
                IOUtil.close( out );
            }
        }

        // Set last modified timestamp
        f.setLastModified( entryDate.getTime() );

        // Set permissions if required
        if ( !isIgnorePermissions() && mode != null && !isDirectory )
        {
            ArchiveEntryUtils.chmod( f, mode );
        }
    }
    catch ( final FileNotFoundException ex )
    {
        String errorMsg = "Failed to extract file: " + entryName + " to directory: " + dir.getAbsolutePath();
        getLogger().error(errorMsg, ex);
        throw new ArchiverException(errorMsg, ex);
    }
}