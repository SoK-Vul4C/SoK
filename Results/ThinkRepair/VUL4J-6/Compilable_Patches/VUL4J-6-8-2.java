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
        int startIndex = offset + 16; // Start index for reading hashed public keys
        int bytesPerHash = this.hashSize * Byte.SIZE / Byte.SIZE; // Calculate the number of bytes per hash
        
        for (int i = 0; i < this.rcount; i++) {
            int keyOffset = startIndex + (i * bytesPerHash);
            if (keyOffset + bytesPerHash <= offset + length) {
                for (int j = 0; j < this.hashSize; j++) {
                    // Perform some computation or operation here using:
                    // data[keyOffset + j]
                    // For example, printing the value:
                    System.out.println("Value: " + data[keyOffset + j]);
                }
            } else {
                // Handle the case where there are not enough bytes in data array for a complete hash
                System.err.println("Not enough bytes in data array for a complete hash at index " + i);
            }
        }
    }
}