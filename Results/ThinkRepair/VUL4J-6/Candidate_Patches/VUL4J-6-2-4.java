public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    int currentPosition = offset + 16; // Starting position for hashed public keys

    if (rcount > 0) {
        if (currentPosition + 2 < data.length) {
            this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, currentPosition));
            currentPosition += 2;

            if (currentPosition + 2 < data.length) {
                this.hashSize = ZipShort.getValue(data, currentPosition);
                currentPosition += 2;

                int expectedSize = currentPosition + rcount * hashSize;
                if (expectedSize <= data.length) {
                    for (int i = 0; i < this.rcount; i++) {
                        for (int j = 0; j < this.hashSize; j++) {
                            if (currentPosition < data.length) {
                                int hashedPublicKey = ZipUtil.signedByteToUnsignedInt(data[currentPosition]);
                                // Process the hashed public keys here
                                currentPosition++;
                            } else {
                                throw new ZipException("Invalid data length or index out of bounds");
                            }
                        }
                    }
                } else {
                    throw new ZipException("Invalid data length or index out of bounds");
                }
            } else {
                throw new ZipException("Invalid data length or index out of bounds");
            }
        } else {
            throw new ZipException("Invalid data length or index out of bounds");
        }
    }
}