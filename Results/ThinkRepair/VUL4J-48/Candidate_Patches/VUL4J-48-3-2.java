private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    if (parts.length < 2 || parts.length > 3) {
        throw new IllegalArgumentException("Invalid JWT format: Expected 2 or 3 parts separated by '.'");
    }

    int index = encodedJWT.lastIndexOf(".");
    if (index == -1) {
        throw new IllegalArgumentException("Invalid JWT format: Missing '.' to separate parts");
    }

    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    // If a signature is provided but verifier is missing
    if (parts.length == 3 && verifier == null) {
        throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
    }

    // A verifier was provided but no signature exists
    if (parts.length == 2 && verifier != null) {
        throw new InvalidJWTSignatureException();
    }

    if (parts.length == 3) {
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    }

    JWT jwt = Mapper.deserialize(base64Decode(parts[1].getBytes(StandardCharsets.UTF_8)), JWT.class);

    if (jwt.isExpired()) {
        throw new JWTExpiredException();
    }

    if (jwt.isUnavailableForProcessing()) {
        throw new JWTUnavailableForProcessingException();
    }

    return jwt;
}