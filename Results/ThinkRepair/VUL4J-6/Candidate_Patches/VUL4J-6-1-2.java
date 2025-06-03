public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);
        
        // Determine the total size of hashed public keys data
        int totalHashedKeysSize = this.rcount * this.hashSize;
        
        // Verify if there is enough data to process
        if (data.length >= offset + 16 + totalHashedKeysSize) {
            // Process hashed public keys
            for (int i = 0; i < this.rcount; i++) {
                // Perform operations on hashed public keys data
                // For example: ZipUtil.signedByteToUnsignedInt(data[offset + 16 + (i * this.hashSize)]);
            }
        } else {
            // Handle insufficient data error
            throw new IllegalStateException("Insufficient data to process hashed public keys");
        }
    }
}