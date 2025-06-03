private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    int index = encodedJWT.lastIndexOf(".");
    if (index == -1) {
        throw new IllegalArgumentException("Invalid encoded JWT: Missing separator '.'");
    }

    // The message comprises the first two segments of the entire JWT, the signature is the last segment.
    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    
    // If a signature is provided and verifier must be provided.
    if (parts.length == 3 && verifier == null) {
        throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
    }

    
    // A verifier was provided but no signature exists, this is treated as an invalid signature.
    if (parts.length == 2 && verifier != null) {
        throw new InvalidJWTSignatureException();
    }

    if (parts.length == 3) {
        // Verify the signature before de-serializing the payload.
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