private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    // Split the encoded JWT into parts based on the dot separator
    String[] jwtParts = encodedJWT.split("\\.");

    if (jwtParts.length < 2) {
        throw new InvalidJWTException("Invalid JWT format");
    }

    byte[] headerBytes = base64Decode(jwtParts[0].getBytes(StandardCharsets.UTF_8));
    byte[] payloadBytes = base64Decode(jwtParts[1].getBytes(StandardCharsets.UTF_8));
    String signature = jwtParts.length > 2 ? jwtParts[2] : ""; // Signature can be empty

    byte[] message = (jwtParts[0] + "." + jwtParts[1]).getBytes(StandardCharsets.UTF_8);

    // If a signature is provided, verify it using the verifier
    if (jwtParts.length > 2 && verifier == null) {
        throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
    }

    // Verify the signature if present
    if (verifier != null) {
        byte[] signatureBytes = base64Decode(signature.getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signatureBytes);
    }

    // Deserialize the JWT payload
    JWT jwt = Mapper.deserialize(payloadBytes, JWT.class);

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