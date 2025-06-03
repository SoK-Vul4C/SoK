private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    // Split the encodedJWT into parts based on the dot separator
    String[] jwtParts = encodedJWT.split("\\.");
    
    if (jwtParts.length < 2) {
        throw new InvalidJWTException("Invalid JWT format");
    }
    
    // The message comprises the first two segments of the entire JWT, the signature is the last segment.
    byte[] message = String.join(".", Arrays.copyOfRange(jwtParts, 0, jwtParts.length - 1)).getBytes(StandardCharsets.UTF_8);
  
    // If a signature is provided and verifier must be provided.
    if (jwtParts.length == 3 && verifier == null) {
        throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
    }
  
    // A verifier was provided but no signature exists, this is treated as an invalid signature.
    if (jwtParts.length == 2 && verifier != null) {
        throw new InvalidJWTSignatureException();
    }
  
    if (jwtParts.length == 3) {
        // Verify the signature before de-serializing the payload.
        byte[] signature = base64Decode(jwtParts[2].getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    }
  
    JWT jwt = Mapper.deserialize(base64Decode(jwtParts[1].getBytes(StandardCharsets.UTF_8)), JWT.class);
  
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