public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);

        int hashKeysStartIndex = offset + 16;
        int numHashKeys = this.rcount;

        // Iterate over hashed public keys
        for (int i = 0; i < numHashKeys; i++) {
            int hashKeyStartIndex = hashKeysStartIndex + (i * this.hashSize);
            for (int j = 0; j < this.hashSize; j++) {
                byte hashedByte = data[hashKeyStartIndex + j];
                // Process the hashed byte here, e.g., add it to a list or perform some operation on it
            }
        }
    }
}