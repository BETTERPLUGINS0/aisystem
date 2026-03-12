package fr.xephi.authme.libs.org.postgresql.plugin;

public enum AuthenticationRequestType {
   CLEARTEXT_PASSWORD,
   GSS,
   MD5_PASSWORD,
   SASL;

   // $FF: synthetic method
   private static AuthenticationRequestType[] $values() {
      return new AuthenticationRequestType[]{CLEARTEXT_PASSWORD, GSS, MD5_PASSWORD, SASL};
   }
}
