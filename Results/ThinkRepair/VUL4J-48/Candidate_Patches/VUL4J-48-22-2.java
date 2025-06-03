private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
    String signaturePart = parts.length > 2 ? parts[2] : null;

    byte[] message;
    if (parts.length > 1) {
        int index = encodedJWT.lastIndexOf(".");
        if (index == -1) {
            throw new IllegalArgumentException("Invalid JWT format. JWT must consist of header, payload, and signature separated by dots.");
        }
        message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);
    } else {
        message = encodedJWT.getBytes(StandardCharsets.UTF_8);
    }

    if (parts.length == 3) {
        if (verifier == null) {
            throw new MissingVerifierException("No Verifier has been provided to verify a signature signed using [" + header.algorithm.getName() + "]");
        }
        byte[] signature = base64Decode(signaturePart.getBytes(StandardCharsets.UTF_8));
        verifier.verify(header.algorithm, message, signature);
    } else if (verifier != null) {
        throw new InvalidJWTSignatureException();
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