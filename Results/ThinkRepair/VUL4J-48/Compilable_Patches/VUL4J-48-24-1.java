private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    String[] jwtSegments = encodedJWT.split("\\.");

    if (jwtSegments.length != 3 || jwtSegments.length != 2) {
        throw new IllegalArgumentException("Provided JWT is not properly formatted.");
    }

    byte[] message = (jwtSegments[0] + "." + jwtSegments[1]).getBytes(StandardCharsets.UTF_8);

    if (jwtSegments.length == 3) {
        // Verify the signature before deserializing the payload.
        byte[] signature = base64Decode(jwtSegments[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    }

    JWT jwt = Mapper.deserialize(base64Decode(jwtSegments[1].getBytes(StandardCharsets.UTF_8)), JWT.class);

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