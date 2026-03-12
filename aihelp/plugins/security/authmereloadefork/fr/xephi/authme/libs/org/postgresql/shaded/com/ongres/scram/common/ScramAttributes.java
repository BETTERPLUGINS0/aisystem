package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.exception.ScramParseException;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.CharAttribute;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import java.util.HashMap;
import java.util.Map;

public enum ScramAttributes implements CharAttribute {
   USERNAME('n'),
   AUTHZID('a'),
   NONCE('r'),
   CHANNEL_BINDING('c'),
   SALT('s'),
   ITERATION('i'),
   CLIENT_PROOF('p'),
   SERVER_SIGNATURE('v'),
   ERROR('e');

   private final char attributeChar;
   private static final Map<Character, ScramAttributes> REVERSE_MAPPING = new HashMap();

   private ScramAttributes(char attributeChar) {
      this.attributeChar = (Character)Preconditions.checkNotNull(attributeChar, "attributeChar");
   }

   public char getChar() {
      return this.attributeChar;
   }

   public static ScramAttributes byChar(char c) throws ScramParseException {
      if (!REVERSE_MAPPING.containsKey(c)) {
         throw new ScramParseException("Attribute with char '" + c + "' does not exist");
      } else {
         return (ScramAttributes)REVERSE_MAPPING.get(c);
      }
   }

   static {
      ScramAttributes[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         ScramAttributes scramAttribute = var0[var2];
         REVERSE_MAPPING.put(scramAttribute.getChar(), scramAttribute);
      }

   }
}
