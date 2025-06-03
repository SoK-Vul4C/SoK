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

        // Iterate over hashed public keys
        for (int i = 0; i < this.rcount; i++) {
            System.out.print("Hashed Public Key " + i + " : ");
            for (int j = 0; j < this.hashSize; j++) {
                int hashedByteIndex = hashKeysStartIndex + (i * this.hashSize) + j;
                if (hashedByteIndex < data.length) {
                    int hashedByte = data[hashedByteIndex] & 0xFF; // Convert byte to unsigned int
                    System.out.print(hashedByte + " ");
                    // Process the hashed byte here, e.g., store it in a list or perform some operation on it
                } else {
                    // Handle the case where the hashed byte index exceeds the data length
                    System.out.print("Data not available");
                }
            }
            System.out.println();
        }
    }
}