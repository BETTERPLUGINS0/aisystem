package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.util.Properties;

public enum SslMode {
   DISABLE("disable"),
   ALLOW("allow"),
   PREFER("prefer"),
   REQUIRE("require"),
   VERIFY_CA("verify-ca"),
   VERIFY_FULL("verify-full");

   public static final SslMode[] VALUES = values();
   public final String value;

   private SslMode(String value) {
      this.value = value;
   }

   public boolean requireEncryption() {
      return this.compareTo(REQUIRE) >= 0;
   }

   public boolean verifyCertificate() {
      return this == VERIFY_CA || this == VERIFY_FULL;
   }

   public boolean verifyPeerName() {
      return this == VERIFY_FULL;
   }

   public static SslMode of(Properties info) throws PSQLException {
      String sslmode = PGProperty.SSL_MODE.getOrDefault(info);
      if (sslmode == null) {
         return !PGProperty.SSL.getBoolean(info) && !"".equals(PGProperty.SSL.getOrDefault(info)) ? PREFER : VERIFY_FULL;
      } else {
         SslMode[] var2 = VALUES;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            SslMode sslMode = var2[var4];
            if (sslMode.value.equalsIgnoreCase(sslmode)) {
               return sslMode;
            }
         }

         throw new PSQLException(GT.tr("Invalid sslmode value: {0}", sslmode), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
      }
   }

   // $FF: synthetic method
   private static SslMode[] $values() {
      return new SslMode[]{DISABLE, ALLOW, PREFER, REQUIRE, VERIFY_CA, VERIFY_FULL};
   }
}
