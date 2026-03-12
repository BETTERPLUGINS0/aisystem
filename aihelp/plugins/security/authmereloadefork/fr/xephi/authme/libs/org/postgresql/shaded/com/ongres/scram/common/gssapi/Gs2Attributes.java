package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.gssapi;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.ScramAttributes;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.CharAttribute;

public enum Gs2Attributes implements CharAttribute {
   CLIENT_NOT(Gs2CbindFlag.CLIENT_NOT.getChar()),
   CLIENT_YES_SERVER_NOT(Gs2CbindFlag.CLIENT_YES_SERVER_NOT.getChar()),
   CHANNEL_BINDING_REQUIRED(Gs2CbindFlag.CHANNEL_BINDING_REQUIRED.getChar()),
   AUTHZID(ScramAttributes.AUTHZID.getChar());

   private final char flag;

   private Gs2Attributes(char flag) {
      this.flag = flag;
   }

   public char getChar() {
      return this.flag;
   }

   public static Gs2Attributes byChar(char c) {
      switch(c) {
      case 'a':
         return AUTHZID;
      case 'n':
         return CLIENT_NOT;
      case 'p':
         return CHANNEL_BINDING_REQUIRED;
      case 'y':
         return CLIENT_YES_SERVER_NOT;
      default:
         throw new IllegalArgumentException("Invalid GS2Attribute character '" + c + "'");
      }
   }

   public static Gs2Attributes byGS2CbindFlag(Gs2CbindFlag cbindFlag) {
      return byChar(cbindFlag.getChar());
   }
}
