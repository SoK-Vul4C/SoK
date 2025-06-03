public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    int currentPosition = offset + 16; // Starting position for hashed public keys

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, currentPosition));
        currentPosition += 2;

        this.hashSize = ZipShort.getValue(data, currentPosition);
        currentPosition += 2;

        if (data.length < currentPosition + rcount * hashSize) {
            throw new ZipException("Invalid data length or index out of bounds");
        }

        // Process hashed public keys
        for (int i = 0; i < this.rcount; i++) {
            for (int j = 0; j < this.hashSize; j++) {
                // Process the hashed public keys here
                int hashedPublicKey = ZipUtil.signedByteToUnsignedInt(data[currentPosition]);
                // Do something with the hashedPublicKey
                currentPosition++;
            }
        }
    }
}