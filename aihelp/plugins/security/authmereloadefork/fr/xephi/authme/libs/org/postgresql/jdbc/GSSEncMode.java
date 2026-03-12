package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.util.Properties;

public enum GSSEncMode {
   DISABLE("disable"),
   ALLOW("allow"),
   PREFER("prefer"),
   REQUIRE("require");

   private static final GSSEncMode[] VALUES = values();
   public final String value;

   private GSSEncMode(String value) {
      this.value = value;
   }

   public boolean requireEncryption() {
      return this.compareTo(REQUIRE) >= 0;
   }

   public static GSSEncMode of(Properties info) throws PSQLException {
      String gssEncMode = PGProperty.GSS_ENC_MODE.getOrDefault(info);
      if (gssEncMode == null) {
         return ALLOW;
      } else {
         GSSEncMode[] var2 = VALUES;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            GSSEncMode mode = var2[var4];
            if (mode.value.equalsIgnoreCase(gssEncMode)) {
               return mode;
            }
         }

         throw new PSQLException(GT.tr("Invalid gssEncMode value: {0}", gssEncMode), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
      }
   }

   // $FF: synthetic method
   private static GSSEncMode[] $values() {
      return new GSSEncMode[]{DISABLE, ALLOW, PREFER, REQUIRE};
   }
}
