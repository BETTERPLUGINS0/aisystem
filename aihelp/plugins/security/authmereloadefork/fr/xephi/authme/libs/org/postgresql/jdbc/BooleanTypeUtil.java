package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.util.logging.Level;
import java.util.logging.Logger;

class BooleanTypeUtil {
   private static final Logger LOGGER = Logger.getLogger(BooleanTypeUtil.class.getName());

   private BooleanTypeUtil() {
   }

   static boolean castToBoolean(Object in) throws PSQLException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.log(Level.FINE, "Cast to boolean: \"{0}\"", String.valueOf(in));
      }

      if (in instanceof Boolean) {
         return (Boolean)in;
      } else if (in instanceof String) {
         return fromString((String)in);
      } else if (in instanceof Character) {
         return fromCharacter((Character)in);
      } else if (in instanceof Number) {
         return fromNumber((Number)in);
      } else {
         throw new PSQLException("Cannot cast to boolean", PSQLState.CANNOT_COERCE);
      }
   }

   static boolean fromString(String strval) throws PSQLException {
      String val = strval.trim();
      if (!"1".equals(val) && !"true".equalsIgnoreCase(val) && !"t".equalsIgnoreCase(val) && !"yes".equalsIgnoreCase(val) && !"y".equalsIgnoreCase(val) && !"on".equalsIgnoreCase(val)) {
         if (!"0".equals(val) && !"false".equalsIgnoreCase(val) && !"f".equalsIgnoreCase(val) && !"no".equalsIgnoreCase(val) && !"n".equalsIgnoreCase(val) && !"off".equalsIgnoreCase(val)) {
            throw cannotCoerceException(strval);
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private static boolean fromCharacter(Character charval) throws PSQLException {
      if ('1' != charval && 't' != charval && 'T' != charval && 'y' != charval && 'Y' != charval) {
         if ('0' != charval && 'f' != charval && 'F' != charval && 'n' != charval && 'N' != charval) {
            throw cannotCoerceException(charval);
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   private static boolean fromNumber(Number numval) throws PSQLException {
      double value = numval.doubleValue();
      if (value == 1.0D) {
         return true;
      } else if (value == 0.0D) {
         return false;
      } else {
         throw cannotCoerceException(numval);
      }
   }

   private static PSQLException cannotCoerceException(Object value) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.log(Level.FINE, "Cannot cast to boolean: \"{0}\"", String.valueOf(value));
      }

      return new PSQLException(GT.tr("Cannot cast to boolean: \"{0}\"", String.valueOf(value)), PSQLState.CANNOT_COERCE);
   }
}
