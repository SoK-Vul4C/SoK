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
        // No need to iterate over the hash values, as they are not being processed
        // Keeping the loop structure for any future updates
        /*
        for (int i = 0; i < this.rcount; i++) {
            for (int j = 0; j < this.hashSize; j++) {
                // ZipUtil.signedByteToUnsignedInt(data[offset + 16 + (i * this.hashSize) + j]));
            }
        }
        */
    }
}