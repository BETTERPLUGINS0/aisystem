package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.gssapi;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.CharAttribute;

public enum Gs2CbindFlag implements CharAttribute {
   CLIENT_NOT('n'),
   CLIENT_YES_SERVER_NOT('y'),
   CHANNEL_BINDING_REQUIRED('p');

   private final char flag;

   private Gs2CbindFlag(char flag) {
      this.flag = flag;
   }

   public char getChar() {
      return this.flag;
   }

   public static Gs2CbindFlag byChar(char c) {
      switch(c) {
      case 'n':
         return CLIENT_NOT;
      case 'p':
         return CHANNEL_BINDING_REQUIRED;
      case 'y':
         return CLIENT_YES_SERVER_NOT;
      default:
         throw new IllegalArgumentException("Invalid Gs2CbindFlag character '" + c + "'");
      }
   }
}
