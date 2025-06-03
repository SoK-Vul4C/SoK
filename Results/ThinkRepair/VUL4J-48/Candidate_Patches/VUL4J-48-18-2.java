private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    // Validate the encoded JWT format
    if (encodedJWT == null || parts.length != 3) {
        throw new IllegalArgumentException("Invalid JWT format. Expected 3 parts separated by '.'");
    }

    int index = encodedJWT.lastIndexOf(".");
    if (index == -1) {
        throw new IllegalArgumentException("Invalid JWT format. Missing '.' separator");
    }

    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    if (parts.length == 3) {
        if (verifier == null) {
            throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.getAlgorithm().getName() + "]");
        }
        
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.getAlgorithm(), message, signature);

        JWT jwt = Mapper.deserialize(base64Decode(parts[1].getBytes(StandardCharsets.UTF_8)), JWT.class);

        if (jwt.isExpired()) {
            throw new JWTExpiredException("JWT has expired");
        }

        if (jwt.isUnavailableForProcessing()) {
            throw new JWTUnavailableForProcessingException("JWT is not available for processing");
        }

        return jwt;
    } else {
        throw new IllegalArgumentException("Invalid JWT format. Expected 3 parts separated by '.'");
    }
}