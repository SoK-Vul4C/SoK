protected void extractFile( final File srcF, final File dir, final InputStream compressedInputStream,
                            final String entryName, final Date entryDate, final boolean isDirectory,
                            final Integer mode, String symlinkDestination )
    throws IOException, ArchiverException
{
    final File destFile = new File(dir, entryName).getCanonicalFile();

    // Check if the destination file is outside the destination directory
    if (!destFile.getCanonicalPath().startsWith(dir.getCanonicalPath() + File.separator)) {
        throw new ArchiverException("Entry is outside the destination directory: " + entryName);
    }

    // Rest of the extraction logic remains unchanged
    // ...
}