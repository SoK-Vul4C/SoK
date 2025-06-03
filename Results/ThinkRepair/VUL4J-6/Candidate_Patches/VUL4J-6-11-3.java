public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    int currentIndex = offset + 16;  // Update current index to start from the correct position
    int hashDataOffset = offset + 16;  // Tracking the offset specific to hash data

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, hashDataOffset));
        this.hashSize = ZipShort.getValue(data, hashDataOffset + 2);

        hashDataOffset += 4;  // Update the hash data offset for correct indexing

        for (int i = 0; i < this.rcount; i++) {
            byte[] hashedPublicKey = new byte[this.hashSize];
            for (int j = 0; j < this.hashSize; j++) {
                if (hashDataOffset < data.length) {
                    hashedPublicKey[j] = data[hashDataOffset];
                    hashDataOffset++;
                } else {
                    // Handle if data index exceeds the array length
                    // You can throw an exception or handle it as needed
                    break;
                }
            }
            // Process the hashed public key data here or store it as needed
        }
    }
}