private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    String[] jwtParts = encodedJWT.split("\\.");
    
    if (jwtParts.length < 2 || jwtParts.length > 3) {
        throw new IllegalArgumentException("Invalid JWT format: " + encodedJWT);
    }

    byte[] message = (jwtParts[0] + "." + jwtParts[1]).getBytes(StandardCharsets.UTF_8);

    if (jwtParts.length == 3) {
        if (verifier == null) {
            throw new MissingVerifierException("No Verifier has been provided for verifying the signature signed using [" + header.algorithm.getName() + "]");
        }
        
        byte[] signature = Base64.getDecoder().decode(jwtParts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    } else if (jwtParts.length == 2 && verifier != null) {
        throw new InvalidJWTSignatureException();
    }

    JWT jwt = Mapper.deserialize(Base64.getDecoder().decode(jwtParts[1].getBytes(StandardCharsets.UTF_8)), JWT.class);

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