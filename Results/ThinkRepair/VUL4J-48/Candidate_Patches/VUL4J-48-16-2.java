private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    int index = encodedJWT.lastIndexOf(".");
    if (index == -1) {
        throw new IllegalArgumentException("Invalid JWT format: Missing dot separator");
    }

    // The message comprises the first two segments of the entire JWT, the signature is the last segment.
    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    // If verifier is required but not provided
    if (parts.length == 3 && verifier == null) {
        throw new MissingVerifierException("No Verifier has been provided to verify a signature signed using ["
                + header.algorithm.getName() + "]");
    }

    // If verifier is provided but no signature exists
    if (parts.length == 2 && verifier != null) {
        throw new InvalidJWTSignatureException();
    }

    if (parts.length == 3) {
        // Verify the signature before deserializing the payload.
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    }

    JWT jwt = Mapper.deserialize(base64Decode(parts[1].getBytes(StandardCharsets.UTF_8)), JWT.class);

    // Verify expiration claim
    if (jwt.isExpired()) {
        throw new JWTExpiredException();
    }

    // Verify the notBefore claim
    if (jwt.isUnavailableForProcessing()) {
        throw new JWTUnavailableForProcessingException();
    }

    return jwt;
}