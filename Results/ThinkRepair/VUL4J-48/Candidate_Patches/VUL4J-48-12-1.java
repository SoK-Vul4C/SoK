private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    int index = encodedJWT.lastIndexOf(".");
    
    byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

    if (parts.length == 3) {
        if (verifier == null) {
            throw new MissingVerifierException("No Verifier has been provided to verify a signature signed using [" + header.algorithm.getName() + "]");
        }
        
        // Verify the signature before de-serializing the payload.
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    } else if (parts.length == 2) {
        if (verifier != null) {
            // A verifier was provided but no signature exists, this is treated as an invalid signature
            throw new InvalidJWTSignatureException();
        }
    } else {
        // Handle the case when parts length is not 2 or 3
        throw new IllegalArgumentException("Invalid JWT format.");
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