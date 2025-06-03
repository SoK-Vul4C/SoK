private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    // Split the encoded JWT into its parts based on the '.' delimiter
    String[] jwtParts = encodedJWT.split("\\.");
    
    if (jwtParts.length < 2 || jwtParts.length > 3) {
        throw new InvalidJWTException("Invalid JWT format");
    }

    // Decode the header and payload parts of the JWT
    String encodedHeader = jwtParts[0];
    String encodedPayload = jwtParts[1];
    
    byte[] decodedHeader = base64Decode(encodedHeader.getBytes(StandardCharsets.UTF_8));
    byte[] decodedPayload = base64Decode(encodedPayload.getBytes(StandardCharsets.UTF_8));
    
    // If a verifier is provided and the JWT has a signature
    if (parts.length == 3 && verifier != null) {
        byte[] message = (encodedHeader + "." + encodedPayload).getBytes(StandardCharsets.UTF_8);
        byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
        
        // Verify the signature
        verifier.verify(header.algorithm, message, signature);
    }
    
    // Deserialize the payload into a JWT object
    JWT jwt = Mapper.deserialize(decodedPayload, JWT.class);

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