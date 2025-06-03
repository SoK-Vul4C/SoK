protected void extractFile(final File srcF, final File dir, final InputStream compressedInputStream,
                            final String entryName, final Date entryDate, final boolean isDirectory,
                            final Integer mode, String symlinkDestination)
    throws IOException, ArchiverException
{
    // Determine the target file for extraction
    final File targetFile = FileUtils.resolveFile(dir, entryName);
    
    // Check if the target file is outside the destination directory
    if (!targetFile.getCanonicalPath().startsWith(dir.getCanonicalPath() + File.separator)) {
        throw new ArchiverException("Entry is outside the target directory: " + entryName);
    }

    // Perform the extraction if the entry is within the target directory
    // Add the extraction logic here
    // Make sure to handle the overwrite, symbolic links, directory creation, file copying, and permissions

    // Set the last modified time of the target file
    targetFile.setLastModified(entryDate.getTime());

    if (!isIgnorePermissions() && mode != null && !isDirectory)
    {
        ArchiveEntryUtils.chmod(targetFile, mode);
    }
}