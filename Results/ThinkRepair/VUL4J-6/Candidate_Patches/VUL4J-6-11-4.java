public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2)); 
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    int currentIndex = offset + 16; // Initialize current index for hash data
    int hashDataOffset = offset + 16; // Initialize hash data offset

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, hashDataOffset));
        this.hashSize = ZipShort.getValue(data, hashDataOffset + 2);
        hashDataOffset += 4; // Move hash data offset after reading hash algorithm and size

        int maxIterations = this.rcount * this.hashSize; // Define maximum iterations based on rcount and hashSize

        int processedCount = 0; // Track the number of processed elements

        for (int i = 0; i < this.rcount && processedCount < maxIterations; i++) {
            byte[] hashedPublicKey = new byte[this.hashSize];
            for (int j = 0; j < this.hashSize && processedCount < maxIterations; j++) {
                if (hashDataOffset < data.length) {
                    hashedPublicKey[j] = data[hashDataOffset];
                    hashDataOffset++;
                    processedCount++;
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