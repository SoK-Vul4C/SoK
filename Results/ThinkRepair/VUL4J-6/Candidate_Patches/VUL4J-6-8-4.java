public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    final int FORMAT_OFFSET = 0;
    final int ALG_ID_OFFSET = 2;
    final int BITLEN_OFFSET = 4;
    final int FLAGS_OFFSET = 6;
    final int RCOUNT_OFFSET = 8;
    final int HASH_ALG_OFFSET = 12;
    final int HASH_SIZE_OFFSET = 14;
    final int HASH_HEADER_SIZE = 16;

    this.format = ZipShort.getValue(data, offset + FORMAT_OFFSET);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + ALG_ID_OFFSET));
    this.bitlen = ZipShort.getValue(data, offset + BITLEN_OFFSET);
    this.flags = ZipShort.getValue(data, offset + FLAGS_OFFSET);
    this.rcount = ZipLong.getValue(data, offset + RCOUNT_OFFSET);

    if (offset + length >= offset + HASH_HEADER_SIZE + this.rcount * this.hashSize) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + HASH_ALG_OFFSET));
        this.hashSize = ZipShort.getValue(data, offset + HASH_SIZE_OFFSET);

        // srlist... hashed public keys
        int keysOffset = offset + HASH_HEADER_SIZE;

        for (int i = 0; i < this.rcount; i++) {
            for (int j = 0; j < this.hashSize; j++) {
                int index = keysOffset + i * this.hashSize + j;
                if (index < data.length) {
                    // Perform some computation or operation here using data[index]
                    // For example, printing the value:
                    System.out.println("Value: " + data[index]);
                } else {
                    System.err.println("Index out of bounds: " + index);
                }
            }
        }
    } else {
        System.err.println("Not enough bytes in data array for key data");
    }
}