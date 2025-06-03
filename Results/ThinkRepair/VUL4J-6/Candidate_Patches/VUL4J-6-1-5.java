public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);

        int hashedKeysDataIndex = offset + 16 + (rcount * hashSize);

        if (data.length >= hashedKeysDataIndex) {
            for (int i = 0; i < rcount; i++) {
                for (int j = 0; j < hashSize; j++) {
                    int keyIndex = offset + 16 + i * hashSize + j;
                    if (keyIndex < data.length) {
                        int hashedKey = ZipUtil.signedByteToUnsignedInt(data[keyIndex]);
                        // Process the hashed key or perform necessary operations
                        System.out.println("Hashed Key: " + hashedKey);
                    } else {
                        throw new IllegalArgumentException("Invalid key index: " + keyIndex);
                    }
                }
            }
        } else {
            throw new IllegalStateException("Insufficient data to process hashed keys");
        }
    }
}