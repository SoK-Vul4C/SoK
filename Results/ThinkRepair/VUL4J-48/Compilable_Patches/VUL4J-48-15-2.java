private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    // The message comprises the first two segments of the entire JWT, the signature is the last segment.
    int index = encodedJWT.lastIndexOf(".");
    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    if (parts.length == 3) {
        // Verify the signature before de-serializing the payload.
        if (verifier == null) {
            throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
        }

        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    } else if (parts.length == 2 && verifier != null) {
        throw new InvalidJWTSignatureException();
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