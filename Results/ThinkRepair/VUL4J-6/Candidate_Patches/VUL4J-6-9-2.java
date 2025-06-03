public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (rcount > 0) {
        int hashAlgOffset = offset + 12;
        if (hashAlgOffset + 2 <= length) {
            this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, hashAlgOffset));
            if (hashAlgOffset + 4 <= length) {
                this.hashSize = ZipShort.getValue(data, hashAlgOffset + 2);
                int hashedPublicKeysOffset = hashAlgOffset + 4;
                int hashedPublicKeysLength = this.rcount * this.hashSize;
                if (hashedPublicKeysOffset + hashedPublicKeysLength <= length) {
                    // Iterate over hashed public keys
                    for (int i = 0; i < this.rcount; i++) {
                        for (int j = 0; j < this.hashSize; j++) {
                            int publicKeyIndex = hashedPublicKeysOffset + i * this.hashSize + j;
                            // Process data element at publicKeyIndex (data[publicKeyIndex])
                        }
                    }
                } else {
                    // Handle insufficient length for hashed public keys
                }
            } else {
                // Handle insufficient length for hash size
            }
        } else {
            // Handle insufficient length for hash algorithm
        }
    }
}