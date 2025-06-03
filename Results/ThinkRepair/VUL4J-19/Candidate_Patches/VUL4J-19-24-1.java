else if( isUserPassword(password.getBytes(passwordCharset), userKey, ownerKey,
                       dicPermissions, documentIDBytes, dicRevision,
                       dicLength, encryptMetadata) )
{
    AccessPermission userAccessPermission = new AccessPermission(dicPermissions);
    setCurrentAccessPermission(userAccessPermission); // Fixed Line
    
    encryptionKey =
        computeEncryptedKey(
            password.getBytes(passwordCharset),
            ownerKey, userKey, oe, ue,
            dicPermissions,
            documentIDBytes,
            dicRevision,
            dicLength,
            encryptMetadata, false );
}