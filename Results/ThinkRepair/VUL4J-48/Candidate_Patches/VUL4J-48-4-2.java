private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    if (parts.length < 2 || parts.length > 3) {
        throw new IllegalArgumentException("Invalid JWT format: Expected 2 or 3 parts, but found " + parts.length);
    }

    int index = encodedJWT.lastIndexOf(".");
    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    if (parts.length == 3) {
        // Verify the signature before de-serializing the payload.
        if (verifier == null) {
            throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
        }
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    } else if (parts.length == 2) {
        if (verifier != null) {
            throw new InvalidJWTSignatureException("Verifier provided but no signature exists");
        }
    }

    JWT jwt = Mapper.deserialize(base64Decode(parts[1].getBytes(StandardCharsets.UTF_8)), JWT.class);

    // Verify expiration claim
    if (jwt.isExpired()) {
        throw new JWTExpiredException("JWT has expired");
    }

    // Verify the notBefore claim
    if (jwt.isUnavailableForProcessing()) {
        throw new JWTUnavailableForProcessingException("JWT is unavailable for processing");
    }

    return jwt;
}