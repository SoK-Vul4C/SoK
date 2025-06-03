public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    if (data.length < offset + 16) {
        // Handle insufficient data in the array
        return;
    }

    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (offset + 16 + this.rcount * (16 + this.hashSize) > data.length) {
        // Handle insufficient data in the array for the expected rcount and hashSize
        return;
    }

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);
        // srlist... hashed public keys
        for (int i = 0; i < this.rcount; i++) {
            for (int j = 0; j < this.hashSize; j++) {
                int dataIndex = offset + 16 + i * (16 + this.hashSize) + j;
                // Handle processing here using data[dataIndex]
            }
        }
    }
}