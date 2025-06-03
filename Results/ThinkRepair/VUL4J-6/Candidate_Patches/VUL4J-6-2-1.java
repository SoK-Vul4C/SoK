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
        int pos = offset + 16;
        for (int i = 0; i < this.rcount; i++) {
            for (int j = 0; j < this.hashSize; j++) {
                if (pos < data.length) {
                    // ZipUtil.signedByteToUnsignedInt(data[pos]);
                    pos++;
                } else {
                    // Handle index out of bounds or incomplete data
                    throw new ZipException("Invalid data length or index out of bounds");
                }
            }
        }
    }
}