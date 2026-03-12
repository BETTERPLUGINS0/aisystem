package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.exception.ScramParseException;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.AbstractCharAttributeValue;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;

public class ScramAttributeValue extends AbstractCharAttributeValue {
   public ScramAttributeValue(ScramAttributes attribute, String value) {
      super(attribute, (String)Preconditions.checkNotNull(value, "value"));
   }

   public static StringBuffer writeTo(StringBuffer sb, ScramAttributes attribute, String value) {
      return (new ScramAttributeValue(attribute, value)).writeTo(sb);
   }

   public static ScramAttributeValue parse(String value) throws ScramParseException {
      if (null != value && value.length() >= 3 && value.charAt(1) == '=') {
         return new ScramAttributeValue(ScramAttributes.byChar(value.charAt(0)), value.substring(2));
      } else {
         throw new ScramParseException("Invalid ScramAttributeValue '" + value + "'");
      }
   }
}
