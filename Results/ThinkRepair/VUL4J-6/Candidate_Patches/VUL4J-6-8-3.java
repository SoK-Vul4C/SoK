public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    this.format = ZipShort.getValue(data, offset);
    this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
    this.bitlen = ZipShort.getValue(data, offset + 4);
    this.flags = ZipShort.getValue(data, offset + 6);
    this.rcount = ZipLong.getValue(data, offset + 8);
    
    int hashHeaderSize = 16;
    
    if (rcount > 0) {
        this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
        this.hashSize = ZipShort.getValue(data, offset + 14);
        
        // srlist... hashed public keys
        int keysOffset = offset + hashHeaderSize;
        
        int keyDataLength = this.rcount * this.hashSize;
        
        // Check if there are enough bytes in data for the required key data
        if (keysOffset + keyDataLength <= offset + length) {
            for (int i = 0; i < this.rcount; i++) {
                for (int j = 0; j < this.hashSize; j++) {
                    int dataIndex = keysOffset + i * this.hashSize + j;
                    if (dataIndex < data.length) {
                        // Perform some computation or operation here using data[dataIndex]
                        // For example, printing the value:
                        System.out.println("Value: " + data[dataIndex]);
                    } else {
                        System.err.println("Index out of bounds: " + dataIndex);
                    }
                }
            }
        } else {
            System.err.println("Not enough bytes in data array for key data");
        }
    }
}