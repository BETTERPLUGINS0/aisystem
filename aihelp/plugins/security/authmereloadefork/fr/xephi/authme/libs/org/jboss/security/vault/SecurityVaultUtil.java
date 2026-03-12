package fr.xephi.authme.libs.org.jboss.security.vault;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.util.StringTokenizer;

public class SecurityVaultUtil {
   public static final String VAULT_PREFIX = "VAULT";

   public static boolean isVaultFormat(char[] chars) {
      if (chars == null) {
         return false;
      } else {
         String str = new String(chars);
         return str.startsWith("VAULT");
      }
   }

   public static boolean isVaultFormat(String str) {
      return str != null && str.startsWith("VAULT");
   }

   public static char[] getValue(String vaultString) throws SecurityVaultException {
      if (!isVaultFormat(vaultString)) {
         throw PicketBoxMessages.MESSAGES.invalidVaultStringFormat(vaultString);
      } else {
         String[] tokens = tokens(vaultString);
         SecurityVault vault = SecurityVaultFactory.get();
         if (!vault.isInitialized()) {
            throw new SecurityVaultException(PicketBoxMessages.MESSAGES.vaultNotInitializedMessage());
         } else {
            return vault.retrieve(tokens[1], tokens[2], tokens[3].getBytes());
         }
      }
   }

   public static String getValueAsString(String vaultString) throws SecurityVaultException {
      char[] val = getValue(vaultString);
      return val != null ? new String(val) : null;
   }

   public static char[] getValue(char[] chars) throws SecurityVaultException {
      if (chars == null) {
         return null;
      } else {
         String vaultString = new String(chars);
         return getValue(vaultString);
      }
   }

   private static String[] tokens(String vaultString) {
      StringTokenizer tokenizer = new StringTokenizer(vaultString, "::");
      int length = tokenizer.countTokens();
      String[] tokens = new String[length];

      for(int var4 = 0; tokenizer != null && tokenizer.hasMoreTokens(); tokens[var4++] = tokenizer.nextToken()) {
      }

      return tokens;
   }
}
