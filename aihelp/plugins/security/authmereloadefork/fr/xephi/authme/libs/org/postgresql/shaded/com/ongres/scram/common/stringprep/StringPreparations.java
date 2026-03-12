package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.stringprep;

import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.saslprep.SaslPrep;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.UsAsciiUtils;

public enum StringPreparations implements StringPreparation {
   NO_PREPARATION {
      protected String doNormalize(String value) throws IllegalArgumentException {
         return UsAsciiUtils.toPrintable(value);
      }
   },
   SASL_PREPARATION {
      protected String doNormalize(String value) throws IllegalArgumentException {
         return SaslPrep.saslPrep(value, true);
      }
   };

   private StringPreparations() {
   }

   protected abstract String doNormalize(String var1) throws IllegalArgumentException;

   public String normalize(String value) throws IllegalArgumentException {
      Preconditions.checkNotEmpty(value, "value");
      String normalized = this.doNormalize(value);
      if (null != normalized && !normalized.isEmpty()) {
         return normalized;
      } else {
         throw new IllegalArgumentException("null or empty value after normalization");
      }
   }

   // $FF: synthetic method
   StringPreparations(Object x2) {
      this();
   }
}
