package fr.xephi.authme.libs.org.jboss.security.vault;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;

public class SecurityVaultFactory {
   private static String defaultVault = "fr.xephi.authme.libs.org.picketbox.plugins.vault.PicketBoxSecurityVault";
   private static SecurityVault vault = null;

   public static SecurityVault get() throws SecurityVaultException {
      return get(defaultVault);
   }

   public static SecurityVault get(String fqn) throws SecurityVaultException {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new RuntimePermission(SecurityVaultFactory.class.getName() + ".get"));
      }

      if (fqn == null) {
         return get();
      } else {
         if (vault == null) {
            Class<?> vaultClass = SecurityActions.loadClass(SecurityVaultFactory.class, fqn);
            if (vaultClass == null) {
               throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToLoadVaultMessage());
            }

            try {
               vault = (SecurityVault)vaultClass.newInstance();
            } catch (Exception var4) {
               throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToCreateVaultMessage(), var4);
            }
         }

         return vault;
      }
   }

   public static SecurityVault get(ClassLoader classLoader, String fqn) throws SecurityVaultException {
      if (classLoader == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("classLoader");
      } else if (fqn == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("fqn");
      } else {
         SecurityManager sm = System.getSecurityManager();
         if (sm != null) {
            sm.checkPermission(new RuntimePermission(SecurityVaultFactory.class.getName() + ".get"));
         }

         if (vault == null) {
            Class<?> vaultClass = SecurityActions.loadClass(classLoader, fqn);
            if (vaultClass == null) {
               throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToLoadVaultMessage());
            }

            try {
               vault = (SecurityVault)vaultClass.newInstance();
            } catch (Exception var5) {
               throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToCreateVaultMessage(), var5);
            }
         }

         return vault;
      }
   }
}
