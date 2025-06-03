private JWT decode(String encodedJWT, Header header, String[] parts, Verifier verifier) {
  int index = encodedJWT.lastIndexOf(".");
  if (index == -1 || parts.length < 2) {
    throw new IllegalArgumentException("Invalid encoded JWT format");
  }

  byte[] message = encodedJWT.substring(0, index).getBytes(StandardCharsets.UTF_8);

  if (parts.length == 3) {
    if (verifier == null) {
      throw new MissingVerifierException("No Verifier has been provided for verifying a signature signed using [" + header.algorithm.getName() + "]");
    }

    byte[] signature = base64Decode(parts[2].getBytes(StandardCharsets.UTF_8));
    verifier.verify(header.algorithm, message, signature);
  } else if (parts.length == 2) {
    if (verifier != null) {
      throw new InvalidJWTSignatureException();
    }
  } else {
    throw new IllegalArgumentException("Invalid encoded JWT format");
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