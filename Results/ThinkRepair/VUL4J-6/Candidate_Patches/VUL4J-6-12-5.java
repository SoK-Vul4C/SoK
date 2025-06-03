public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);

        int hashKeysStartIndex = offset + 16;
        int totalKeysSize = this.rcount * this.hashSize;
        
        if (hashKeysStartIndex + totalKeysSize <= data.length) {
            // Iterate over hashed public keys
            for (int i = 0; i < this.rcount; i++) {
                System.out.print("Hashed Public Key " + i + " : ");
                for (int j = 0; j < this.hashSize; j++) {
                    int hashedByteIndex = hashKeysStartIndex + (i * this.hashSize) + j;
                    int hashedByte = data[hashedByteIndex] & 0xFF; // Convert byte to unsigned int
                    System.out.print(hashedByte + " ");
                    // Process the hashed byte here, e.g., store it in a list or perform some operation on it
                }
                System.out.println();
            }
        } else {
            System.out.println("Invalid data length for hashed keys");
        }
    }
}