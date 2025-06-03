public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);
      
        // srlist... hashed public keys
        int publicKeyDataStartIndex = offset + 16;
        int publicKeyDataLength = this.rcount * this.hashSize;
        
        if (publicKeyDataStartIndex + publicKeyDataLength <= data.length) {
            // Iterate over the hashed public keys
            for (int i = 0; i < publicKeyDataLength; i++) {
                // Process each hashed public key data
                // ZipUtil.signedByteToUnsignedInt(data[publicKeyDataStartIndex + i]);
            }
        } else {
            // Handle the case where there is not enough data for the hashed public keys
        }
    }
}