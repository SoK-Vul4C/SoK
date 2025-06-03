public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);

    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);
        
        int rcountOffset = 16;
      
        if (offset + rcountOffset + this.rcount * this.hashSize <= data.length) {
            // srlist... hashed public keys
            for (int i = 0; i < this.rcount; i++) {
                for (int j = 0; j < this.hashSize; j++) {
                    // Access hashed public key data at index offset + rcountOffset + i * this.hashSize + j
                    int currentIndex = offset + rcountOffset + i * this.hashSize + j;
                    byte hashedData = data[currentIndex];
                    // Process the hashed data as needed
                }
            }
        } else {
            // Handle the case where there is not enough data for the hashed public keys
        }
    }
}