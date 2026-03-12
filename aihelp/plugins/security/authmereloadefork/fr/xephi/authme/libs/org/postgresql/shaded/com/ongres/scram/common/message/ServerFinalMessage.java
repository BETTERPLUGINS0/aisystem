package fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.message;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.ScramAttributeValue;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.ScramAttributes;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.ScramStringFormatting;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.exception.ScramParseException;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.StringWritable;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.util.StringWritableCsv;
import java.util.HashMap;
import java.util.Map;

public class ServerFinalMessage implements StringWritable {
   private final byte[] verifier;
   private final ServerFinalMessage.Error error;

   public ServerFinalMessage(byte[] verifier) throws IllegalArgumentException {
      this.verifier = (byte[])Preconditions.checkNotNull(verifier, "verifier");
      this.error = null;
   }

   public ServerFinalMessage(ServerFinalMessage.Error error) throws IllegalArgumentException {
      this.error = (ServerFinalMessage.Error)Preconditions.checkNotNull(error, "error");
      this.verifier = null;
   }

   public boolean isError() {
      return null != this.error;
   }

   @SuppressFBWarnings({"EI_EXPOSE_REP"})
   public byte[] getVerifier() {
      return this.verifier;
   }

   public ServerFinalMessage.Error getError() {
      return this.error;
   }

   public StringBuffer writeTo(StringBuffer sb) {
      return StringWritableCsv.writeTo(sb, this.isError() ? new ScramAttributeValue(ScramAttributes.ERROR, this.error.errorMessage) : new ScramAttributeValue(ScramAttributes.SERVER_SIGNATURE, ScramStringFormatting.base64Encode(this.verifier)));
   }

   public static ServerFinalMessage parseFrom(String serverFinalMessage) throws ScramParseException, IllegalArgumentException {
      Preconditions.checkNotEmpty(serverFinalMessage, "serverFinalMessage");
      String[] attributeValues = StringWritableCsv.parseFrom(serverFinalMessage, 1, 0);
      if (attributeValues != null && attributeValues.length == 1) {
         ScramAttributeValue attributeValue = ScramAttributeValue.parse(attributeValues[0]);
         if (ScramAttributes.SERVER_SIGNATURE.getChar() == attributeValue.getChar()) {
            byte[] verifier = ScramStringFormatting.base64Decode(attributeValue.getValue());
            return new ServerFinalMessage(verifier);
         } else if (ScramAttributes.ERROR.getChar() == attributeValue.getChar()) {
            return new ServerFinalMessage(ServerFinalMessage.Error.getByErrorMessage(attributeValue.getValue()));
         } else {
            throw new ScramParseException("Invalid server-final-message: it must contain either a verifier or an error attribute");
         }
      } else {
         throw new ScramParseException("Invalid server-final-message");
      }
   }

   public String toString() {
      return this.writeTo(new StringBuffer()).toString();
   }

   public static enum Error {
      INVALID_ENCODING("invalid-encoding"),
      EXTENSIONS_NOT_SUPPORTED("extensions-not-supported"),
      INVALID_PROOF("invalid-proof"),
      CHANNEL_BINDINGS_DONT_MATCH("channel-bindings-dont-match"),
      SERVER_DOES_SUPPORT_CHANNEL_BINDING("server-does-support-channel-binding"),
      CHANNEL_BINDING_NOT_SUPPORTED("channel-binding-not-supported"),
      UNSUPPORTED_CHANNEL_BINDING_TYPE("unsupported-channel-binding-type"),
      UNKNOWN_USER("unknown-user"),
      INVALID_USERNAME_ENCODING("invalid-username-encoding"),
      NO_RESOURCES("no-resources"),
      OTHER_ERROR("other-error");

      private static final Map<String, ServerFinalMessage.Error> BY_NAME_MAPPING = valuesAsMap();
      private final String errorMessage;

      private Error(String errorMessage) {
         this.errorMessage = errorMessage;
      }

      public String getErrorMessage() {
         return this.errorMessage;
      }

      public static ServerFinalMessage.Error getByErrorMessage(String errorMessage) throws IllegalArgumentException {
         Preconditions.checkNotEmpty(errorMessage, "errorMessage");
         if (!BY_NAME_MAPPING.containsKey(errorMessage)) {
            throw new IllegalArgumentException("Invalid error message '" + errorMessage + "'");
         } else {
            return (ServerFinalMessage.Error)BY_NAME_MAPPING.get(errorMessage);
         }
      }

      private static Map<String, ServerFinalMessage.Error> valuesAsMap() {
         Map<String, ServerFinalMessage.Error> map = new HashMap(values().length);
         ServerFinalMessage.Error[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            ServerFinalMessage.Error error = var1[var3];
            map.put(error.errorMessage, error);
         }

         return map;
      }
   }
}
