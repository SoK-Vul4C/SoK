private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    int index = encodedJWT.lastIndexOf("."); // Fixed Line

    if (index == -1) {
        throw new IllegalArgumentException("Invalid JWT format. JWT must consist of header, payload, and signature separated by dots.");
    }

    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    if (parts.length == 3 && verifier == null) {
        throw new MissingVerifierException("No Verifier has been provided to verify a signature signed using [" + header.algorithm.getName() + "]");
    }

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