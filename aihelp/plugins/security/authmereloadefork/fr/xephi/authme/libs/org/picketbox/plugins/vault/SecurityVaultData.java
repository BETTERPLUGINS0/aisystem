package fr.xephi.authme.libs.org.picketbox.plugins.vault;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SecurityVaultData implements Serializable {
   private static final long serialVersionUID = 1L;
   private static final int VERSION = 1;
   private transient Map<String, byte[]> vaultData = new ConcurrentHashMap();

   private void writeObject(ObjectOutputStream oos) throws IOException {
      oos.writeObject(new Integer(1));
      oos.writeObject(this.vaultData);
   }

   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
      int version = (Integer)ois.readObject();
      if (PicketBoxLogger.LOGGER.isDebugEnabled()) {
         PicketBoxLogger.LOGGER.securityVaultContentVersion(String.valueOf(version), String.valueOf(1));
      }

      if (version == 1) {
         this.vaultData = (Map)ois.readObject();
      } else {
         throw PicketBoxMessages.MESSAGES.unrecognizedVaultContentVersion(String.valueOf(version), "1", String.valueOf(1));
      }
   }

   byte[] getVaultData(String keyAlias, String vaultBlock, String attributeName) {
      return (byte[])this.vaultData.get(dataKey(keyAlias, vaultBlock, attributeName));
   }

   void addVaultData(String keyAlias, String vaultBlock, String attributeName, byte[] encryptedData) {
      this.vaultData.put(dataKey(keyAlias, vaultBlock, attributeName), encryptedData);
   }

   boolean deleteVaultData(String keyAlias, String vaultBlock, String attributeName) {
      return this.vaultData.remove(dataKey(keyAlias, vaultBlock, attributeName)) != null;
   }

   Set<String> getVaultDataKeys() {
      return this.vaultData.keySet();
   }

   private static String dataKey(String keyAlias, String vaultBlock, String attributeName) {
      return vaultBlock + "::" + attributeName;
   }
}
