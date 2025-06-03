public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    if (offset + 16 < data.length) {
        this.format = ZipShort.getValue(data, offset);
        this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
        this.bitlen = ZipShort.getValue(data, offset + 4);
        this.flags = ZipShort.getValue(data, offset + 6);
        this.rcount = ZipLong.getValue(data, offset + 8);

        if (offset + 16 + this.rcount * this.hashSize < data.length) {
            if (rcount > 0) {
                this.hashAlg = HashAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 12));
                this.hashSize = ZipShort.getValue(data, offset + 14);
                // srlist... hashed public keys
                for (int i = 0; i < this.rcount; i++) {
                    for (int j = 0; j < this.hashSize; j++) {
                        int index = offset + 16 + i * this.hashSize + j;
                        if (index < data.length) {
                            int value = ZipUtil.signedByteToUnsignedInt(data[index]);
                            // Process each byte if needed
                        } else {
                            // Handle out of bounds index
                        }
                    }
                }
            }
        } else {
            // Handle out of bounds index for hash values
        }
    } else {
        // Handle out of bounds index for header fields
    }
}