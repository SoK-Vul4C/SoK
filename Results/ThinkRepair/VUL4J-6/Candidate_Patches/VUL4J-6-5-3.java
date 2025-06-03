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
        
        int hashDataOffset = offset + 16;
        int publicKeySize = this.rcount * this.hashSize;
        
        for (int i = 0; i < publicKeySize; i++) {
            int publicKeyIndex = hashDataOffset + i;
            // Process the hashed public key data here
            // ZipUtil.signedByteToUnsignedInt(data[publicKeyIndex]);
        }
    }
}