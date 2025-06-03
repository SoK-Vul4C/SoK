public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);

        // Calculate the start index for hashed keys data
        int hashedKeysStartIndex = offset + 16;

        // Calculate the total size of hashed keys data
        int totalHashedKeysSize = this.rcount * this.hashSize;

        // Check if there is enough data to process
        if (data.length >= hashedKeysStartIndex + totalHashedKeysSize) {
            // Process hashed keys data
            int dataIndex = hashedKeysStartIndex;
            for (int i = 0; i < this.rcount; i++) {
                for (int j = 0; j < this.hashSize; j++) {
                    // Process the hashed key at the current index
                    int hashedKey = ZipUtil.signedByteToUnsignedInt(data[dataIndex]);
                    // Perform necessary operations with the hashed key here
                    dataIndex++;
                }
            }
        } else {
            // Handle error if there is insufficient data
            throw new IllegalStateException("Insufficient data to process hashed keys");
        }
    }
}