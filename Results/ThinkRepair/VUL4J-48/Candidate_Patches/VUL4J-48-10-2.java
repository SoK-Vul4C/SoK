private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    if (parts.length < 2 || parts.length > 3) {
        throw new IllegalArgumentException("Invalid JWT format. Expected either 2 or 3 parts but found: " + parts.length);
    }

    boolean isSignedJWT = parts.length == 3;
    
    if (isSignedJWT && verifier == null) {
        throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
    }

    if (isSignedJWT) {
        byte[] message = encodedJWT.substring(0, encodedJWT.lastIndexOf(".")).getBytes(StandardCharsets.UTF_8);
        
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