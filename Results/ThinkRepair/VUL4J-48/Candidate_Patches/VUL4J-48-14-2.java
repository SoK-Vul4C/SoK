private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    int index = encodedJWT.lastIndexOf(".");
    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    // Check if the JWT has a valid number of parts
    if (parts.length == 3) {
        // If a signature is provided, verifier must be provided
        if (verifier == null) {
            throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
        }

        // Verify the signature before deserializing the payload
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    } else if (parts.length == 2) {
        // If there is no signature, verifier should not be provided
        if (verifier != null) {
            throw new MissingVerifierException("Verifier should not be provided when no signature is present");
        }
    } else {
        throw new IllegalArgumentException("Invalid JWT format: Unexpected number of parts");
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