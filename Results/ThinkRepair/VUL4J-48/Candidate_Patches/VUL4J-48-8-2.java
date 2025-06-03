private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    int index = encodedJWT.lastIndexOf(".");

    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    if (parts.length == 3) {
        // Verify the signature before proceeding
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));

        if (verifier == null) {
            throw new MissingVerifierException("No Verifier has been provided to verify a signature signed using [" + header.algorithm.getName() + "]");
        }

        verifier.verify(header.algorithm, message, signature);
    } else if (parts.length == 2) {
        // Verifier is provided but no signature exists
        if (verifier != null) {
            throw new InvalidJWTSignatureException();
        }
    } else {
        throw new IllegalArgumentException("Invalid JWT format");
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