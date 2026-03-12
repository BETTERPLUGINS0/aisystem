package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.plugin.AuthenticationPlugin;
import fr.xephi.authme.libs.org.postgresql.plugin.AuthenticationRequestType;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.ObjectFactory;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

class AuthenticationPluginManager {
   private static final Logger LOGGER = Logger.getLogger(AuthenticationPluginManager.class.getName());

   private AuthenticationPluginManager() {
   }

   public static <T> T withPassword(AuthenticationRequestType type, Properties info, AuthenticationPluginManager.PasswordAction<char[], T> action) throws PSQLException, IOException {
      char[] password = null;
      String authPluginClassName = PGProperty.AUTHENTICATION_PLUGIN_CLASS_NAME.getOrDefault(info);
      if (authPluginClassName != null && !"".equals(authPluginClassName)) {
         AuthenticationPlugin authPlugin;
         try {
            authPlugin = (AuthenticationPlugin)ObjectFactory.instantiate(AuthenticationPlugin.class, authPluginClassName, info, false, (String)null);
         } catch (Exception var11) {
            String msg = GT.tr("Unable to load Authentication Plugin {0}", authPluginClassName);
            LOGGER.log(Level.FINE, msg, var11);
            throw new PSQLException(msg, PSQLState.INVALID_PARAMETER_VALUE, var11);
         }

         password = authPlugin.getPassword(type);
      } else {
         String passwordText = PGProperty.PASSWORD.getOrDefault(info);
         if (passwordText != null) {
            password = passwordText.toCharArray();
         }
      }

      Object var14;
      try {
         var14 = action.apply(password);
      } finally {
         if (password != null) {
            Arrays.fill(password, '\u0000');
         }

      }

      return var14;
   }

   public static <T> T withEncodedPassword(AuthenticationRequestType type, Properties info, AuthenticationPluginManager.PasswordAction<byte[], T> action) throws PSQLException, IOException {
      byte[] encodedPassword = (byte[])withPassword(type, info, (password) -> {
         if (password == null) {
            throw new PSQLException(GT.tr("The server requested password-based authentication, but no password was provided by plugin {0}", PGProperty.AUTHENTICATION_PLUGIN_CLASS_NAME.getOrDefault(info)), PSQLState.CONNECTION_REJECTED);
         } else {
            ByteBuffer buf = StandardCharsets.UTF_8.encode(CharBuffer.wrap(password));
            byte[] bytes = new byte[buf.limit()];
            buf.get(bytes);
            return bytes;
         }
      });

      Object var4;
      try {
         var4 = action.apply(encodedPassword);
      } finally {
         Arrays.fill(encodedPassword, (byte)0);
      }

      return var4;
   }

   @FunctionalInterface
   public interface PasswordAction<T, R> {
      R apply(T var1) throws PSQLException, IOException;
   }
}
