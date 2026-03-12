package fr.xephi.authme.security;

public enum MessageDigestAlgorithm {
   MD5("MD5"),
   SHA1("SHA-1"),
   SHA256("SHA-256"),
   SHA512("SHA-512");

   private final String key;

   private MessageDigestAlgorithm(String param3) {
      this.key = key;
   }

   public String getKey() {
      return this.key;
   }

   // $FF: synthetic method
   private static MessageDigestAlgorithm[] $values() {
      return new MessageDigestAlgorithm[]{MD5, SHA1, SHA256, SHA512};
   }
}
