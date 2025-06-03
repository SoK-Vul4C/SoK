private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    int index = encodedJWT.lastIndexOf(".");
    
    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);
    
    if (parts.length < 2 || parts.length > 3) {
        throw new IllegalArgumentException("Invalid JWT format. Expected 2 or 3 parts separated by '.'");
    }

    if (parts.length == 3 && verifier == null) {
        throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.getAlgorithm().getName() + "]");
    }

    if (parts.length == 2 && verifier != null) {
        throw new InvalidJWTSignatureException("Verifier provided but no signature exists, this is treated as an invalid signature");
    }

    if (parts.length == 3) {
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.getAlgorithm(), message, signature);
    }

    JWT jwt = Mapper.deserialize(base64Decode(parts[1].getBytes(StandardCharsets.UTF_8)), JWT.class);

    if (jwt.isExpired()) {
        throw new JWTExpiredException("JWT has expired");
    }

    if (jwt.isUnavailableForProcessing()) {
        throw new JWTUnavailableForProcessingException("JWT is not available for processing");
    }

    return jwt;
}