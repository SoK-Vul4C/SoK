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
        int startIndex = offset + 16; // Start index of hashed public keys
        int totalHashSize = rcount * hashSize; // Total size of hashed public keys

        for (int i = 0; i < rcount; i++) {
            for (int j = 0; j < hashSize; j++) {
                int dataIndex = startIndex + (i * hashSize) + j;
                byte hashedPublicKey = data[dataIndex];
                // Process the hashed public key here, e.g., ZipUtil.signedByteToUnsignedInt(hashedPublicKey)
            }
        }
    }
}