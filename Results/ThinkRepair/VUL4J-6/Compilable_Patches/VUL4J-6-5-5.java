public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);

        int hashDataStartIndex = offset + 16;

        for (int i = 0; i < this.rcount; i++) {
            int publicKeyStartIndex = hashDataStartIndex + i * this.hashSize;
            
            // Ensure the publicKeyStartIndex is within the bounds of the data array
            if (publicKeyStartIndex + this.hashSize > data.length) {
                throw new IllegalArgumentException("Invalid hash data indices");
            }
            
            // Process the hashed public key data efficiently
            for (int j = 0; j < this.hashSize; j++) {
                int dataIndex = publicKeyStartIndex + j;
                // Process the hashed public key data here
                // ZipUtil.signedByteToUnsignedInt(data[dataIndex]);
            }
        }
    }
}