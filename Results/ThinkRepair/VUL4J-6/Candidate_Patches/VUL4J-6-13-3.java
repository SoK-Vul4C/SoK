public void parseCentralDirectoryFormat(final byte[] data, final int offset, final int length) {
    if (offset + 16 < data.length) {
        this.format = ZipShort.getValue(data, offset);
        this.algId = EncryptionAlgorithm.getAlgorithmByCode(ZipShort.getValue(data, offset + 2));
        this.bitlen = ZipShort.getValue(data, offset + 4);
        this.flags = ZipShort.getValue(data, offset + 6);
        this.rcount = ZipLong.getValue(data, offset + 8);

        if (offset + 16 + this.rcount * this.hashSize + 2 < data.length) {
            if (rcount > 0) {
                int startIndex = offset + 16;
                int endIndex = Math.min(startIndex + this.rcount * this.hashSize, data.length - 2); // Ensure we do not go beyond data array boundaries

                for (int i = startIndex; i < endIndex; i += this.hashSize) {
                    for (int j = i; j < Math.min(i + this.hashSize, endIndex); j++) {
                        int value = ZipUtil.signedByteToUnsignedInt(data[j]);
                        // Process each byte if needed
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