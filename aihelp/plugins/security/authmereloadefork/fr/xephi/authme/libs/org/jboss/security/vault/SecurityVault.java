package fr.xephi.authme.libs.org.jboss.security.vault;

import java.util.Map;
import java.util.Set;

public interface SecurityVault {
   void init(Map<String, Object> var1) throws SecurityVaultException;

   boolean isInitialized();

   byte[] handshake(Map<String, Object> var1) throws SecurityVaultException;

   Set<String> keyList() throws SecurityVaultException;

   boolean exists(String var1, String var2) throws SecurityVaultException;

   void store(String var1, String var2, char[] var3, byte[] var4) throws SecurityVaultException;

   char[] retrieve(String var1, String var2, byte[] var3) throws SecurityVaultException;

   boolean remove(String var1, String var2, byte[] var3) throws SecurityVaultException;
}
