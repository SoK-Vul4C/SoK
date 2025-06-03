private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    int index = encodedJWT.lastIndexOf(".");
    if (index == -1) {
        throw new IllegalArgumentException("Invalid encoded JWT: Missing separator '.'");
    }

    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    // If a signature is provided but no verifier is provided, throw MissingVerifierException
    if (parts.length == 3 && verifier == null) {
        throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
    }

    // If no signature is provided but verifier is present, throw InvalidJWTSignatureException
    if (parts.length == 2 && verifier != null) {
        throw new InvalidJWTSignatureException();
    }

    // Verify the signature if signature is present
    if (parts.length == 3) {
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    }

    // Deserialize the payload
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