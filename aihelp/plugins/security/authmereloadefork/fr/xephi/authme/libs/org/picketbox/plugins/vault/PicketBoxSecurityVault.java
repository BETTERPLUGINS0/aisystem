package fr.xephi.authme.libs.org.picketbox.plugins.vault;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.Util;
import fr.xephi.authme.libs.org.jboss.security.plugins.PBEUtils;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVault;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultException;
import fr.xephi.authme.libs.org.picketbox.util.EncryptionUtil;
import fr.xephi.authme.libs.org.picketbox.util.KeyStoreUtil;
import fr.xephi.authme.libs.org.picketbox.util.StringUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.SecretKeyEntry;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PicketBoxSecurityVault implements SecurityVault {
   protected boolean finishedInit = false;
   protected KeyStore keystore = null;
   protected String encryptionAlgorithm = "AES";
   protected int keySize = 128;
   private char[] keyStorePWD = null;
   private String alias = null;
   private SecurityVaultData vaultContent = null;
   private SecretKey adminKey;
   private String decodedEncFileDir;
   private boolean createKeyStore = false;
   private String keyStoreType = "JCEKS";
   public static final String ENC_FILE_DIR = "ENC_FILE_DIR";
   public static final String KEYSTORE_URL = "KEYSTORE_URL";
   public static final String KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD";
   public static final String KEYSTORE_ALIAS = "KEYSTORE_ALIAS";
   public static final String SALT = "SALT";
   public static final String ITERATION_COUNT = "ITERATION_COUNT";
   public static final String PASS_MASK_PREFIX = "MASK-";
   public static final String PUBLIC_CERT = "PUBLIC_CERT";
   public static final String KEY_SIZE = "KEY_SIZE";
   public static final String CREATE_KEYSTORE = "CREATE_KEYSTORE";
   public static final String KEYSTORE_TYPE = "KEYSTORE_TYPE";
   private static final String ENCODED_FILE = "ENC.dat";
   private static final String SHARED_KEY_FILE = "Shared.dat";
   private static final String ADMIN_KEY = "ADMIN_KEY";
   protected static final String VAULT_CONTENT_FILE = "VAULT.dat";
   protected static final String defaultKeyStoreType = "JCEKS";

   public void init(Map<String, Object> options) throws SecurityVaultException {
      if (options != null && !options.isEmpty()) {
         String keystoreURL = (String)options.get("KEYSTORE_URL");
         if (keystoreURL == null) {
            throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage("KEYSTORE_URL"));
         } else {
            if (keystoreURL.contains("${")) {
               keystoreURL = keystoreURL.replaceAll(":", "::");
            }

            keystoreURL = StringUtil.getSystemPropertyAsString(keystoreURL);
            String password = (String)options.get("KEYSTORE_PASSWORD");
            if (password == null) {
               throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage("KEYSTORE_PASSWORD"));
            } else if (!password.startsWith("MASK-") && !Util.isPasswordCommand(password)) {
               throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidKeystorePasswordFormatMessage());
            } else {
               String salt = (String)options.get("SALT");
               if (salt == null) {
                  throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage("SALT"));
               } else {
                  String iterationCountStr = (String)options.get("ITERATION_COUNT");
                  if (iterationCountStr == null) {
                     throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage("ITERATION_COUNT"));
                  } else {
                     int iterationCount = Integer.parseInt(iterationCountStr);
                     this.alias = (String)options.get("KEYSTORE_ALIAS");
                     if (this.alias == null) {
                        throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage("KEYSTORE_ALIAS"));
                     } else {
                        String keySizeStr = (String)options.get("KEY_SIZE");
                        if (keySizeStr != null) {
                           this.keySize = Integer.parseInt(keySizeStr);
                        }

                        String encFileDir = (String)options.get("ENC_FILE_DIR");
                        if (encFileDir == null) {
                           throw new SecurityVaultException(PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMessage("ENC_FILE_DIR"));
                        } else {
                           this.createKeyStore = options.get("CREATE_KEYSTORE") != null ? Boolean.parseBoolean((String)options.get("CREATE_KEYSTORE")) : this.createKeyStore;
                           this.keyStoreType = options.get("KEYSTORE_TYPE") != null ? (String)options.get("KEYSTORE_TYPE") : "JCEKS";

                           try {
                              this.keyStorePWD = this.loadKeystorePassword(password, salt, iterationCount);
                              this.keystore = this.getKeyStore(keystoreURL);
                              this.checkAndConvertKeyStoreToJCEKS(keystoreURL);
                           } catch (Exception var10) {
                              throw new SecurityVaultException(var10);
                           }

                           this.readVaultContent(keystoreURL, encFileDir);
                           PicketBoxLogger.LOGGER.infoVaultInitialized();
                           this.finishedInit = true;
                        }
                     }
                  }
               }
            }
         }
      } else {
         throw PicketBoxMessages.MESSAGES.invalidNullOrEmptyOptionMap("options");
      }
   }

   public boolean isInitialized() {
      return this.finishedInit;
   }

   public byte[] handshake(Map<String, Object> handshakeOptions) throws SecurityVaultException {
      return new byte[this.keySize];
   }

   public Set<String> keyList() throws SecurityVaultException {
      return this.vaultContent.getVaultDataKeys();
   }

   public void store(String vaultBlock, String attributeName, char[] attributeValue, byte[] sharedKey) throws SecurityVaultException {
      if (StringUtil.isNullOrEmpty(vaultBlock)) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("vaultBlock");
      } else if (StringUtil.isNullOrEmpty(attributeName)) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("attributeName");
      } else {
         String av = new String(attributeValue);
         EncryptionUtil util = new EncryptionUtil(this.encryptionAlgorithm, this.keySize);

         try {
            SecretKeySpec sKeySpec = new SecretKeySpec(this.adminKey.getEncoded(), this.encryptionAlgorithm);
            byte[] encryptedData = util.encrypt(av.getBytes(), sKeySpec);
            this.vaultContent.addVaultData(this.alias, vaultBlock, attributeName, encryptedData);
         } catch (Exception var10) {
            throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToEncryptDataMessage(), var10);
         }

         try {
            this.writeVaultData();
         } catch (IOException var9) {
            throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToWriteVaultDataFileMessage("VAULT.dat"), var9);
         }
      }
   }

   public char[] retrieve(String vaultBlock, String attributeName, byte[] sharedKey) throws SecurityVaultException {
      if (StringUtil.isNullOrEmpty(vaultBlock)) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("vaultBlock");
      } else if (StringUtil.isNullOrEmpty(attributeName)) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("attributeName");
      } else {
         byte[] encryptedValue = this.vaultContent.getVaultData(this.alias, vaultBlock, attributeName);
         SecretKeySpec secretKeySpec = new SecretKeySpec(this.adminKey.getEncoded(), this.encryptionAlgorithm);
         EncryptionUtil encUtil = new EncryptionUtil(this.encryptionAlgorithm, this.keySize);

         try {
            return (new String(encUtil.decrypt(encryptedValue, secretKeySpec))).toCharArray();
         } catch (Exception var8) {
            throw new SecurityVaultException(var8);
         }
      }
   }

   public boolean exists(String vaultBlock, String attributeName) throws SecurityVaultException {
      return this.vaultContent.getVaultData(this.alias, vaultBlock, attributeName) != null;
   }

   public boolean remove(String vaultBlock, String attributeName, byte[] sharedKey) throws SecurityVaultException {
      if (StringUtil.isNullOrEmpty(vaultBlock)) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("vaultBlock");
      } else if (StringUtil.isNullOrEmpty(attributeName)) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("attributeName");
      } else {
         try {
            if (this.vaultContent.deleteVaultData(this.alias, vaultBlock, attributeName)) {
               this.writeVaultData();
               return true;
            } else {
               return false;
            }
         } catch (IOException var5) {
            throw new SecurityVaultException(PicketBoxMessages.MESSAGES.unableToWriteVaultDataFileMessage("VAULT.dat"), var5);
         } catch (Exception var6) {
            throw new SecurityVaultException(var6);
         }
      }
   }

   private char[] loadKeystorePassword(String passwordDef, String salt, int iterationCount) throws Exception {
      char[] password;
      if (passwordDef.startsWith("MASK-")) {
         String keystorePass = this.decode(passwordDef, salt, iterationCount);
         password = keystorePass.toCharArray();
      } else {
         password = Util.loadPassword(passwordDef);
      }

      return password;
   }

   private String decode(String maskedString, String salt, int iterationCount) throws Exception {
      String pbeAlgo = "PBEwithMD5andDES";
      if (maskedString.startsWith("MASK-")) {
         SecretKeyFactory factory = SecretKeyFactory.getInstance(pbeAlgo);
         char[] password = "somearbitrarycrazystringthatdoesnotmatter".toCharArray();
         PBEParameterSpec cipherSpec = new PBEParameterSpec(salt.getBytes(), iterationCount);
         PBEKeySpec keySpec = new PBEKeySpec(password);
         SecretKey cipherKey = factory.generateSecret(keySpec);
         maskedString = maskedString.substring("MASK-".length());
         String decodedValue = PBEUtils.decode64(maskedString, pbeAlgo, cipherKey, cipherSpec);
         maskedString = decodedValue;
      }

      return maskedString;
   }

   private void setUpVault(String keystoreURL, String decodedEncFileDir) throws NoSuchAlgorithmException, IOException {
      this.vaultContent = new SecurityVaultData();
      this.writeVaultData();
      SecretKey sk = this.getAdminKey();
      if (sk != null) {
         this.adminKey = sk;
      } else {
         EncryptionUtil util = new EncryptionUtil(this.encryptionAlgorithm, this.keySize);
         sk = util.generateKey();
         SecretKeyEntry skEntry = new SecretKeyEntry(sk);

         try {
            this.keystore.setEntry(this.alias, skEntry, new PasswordProtection(this.keyStorePWD));
            this.adminKey = sk;
            this.saveKeyStoreToFile(keystoreURL);
         } catch (KeyStoreException var7) {
            throw PicketBoxMessages.MESSAGES.noSecretKeyandAliasAlreadyUsed(this.alias);
         } catch (Exception var8) {
            throw PicketBoxMessages.MESSAGES.unableToStoreKeyStoreToFile(var8, keystoreURL);
         }
      }

   }

   private void writeVaultData() throws IOException {
      FileOutputStream fos = null;
      ObjectOutputStream oos = null;

      try {
         fos = new FileOutputStream(this.decodedEncFileDir + "VAULT.dat");
         oos = new ObjectOutputStream(fos);
         oos.writeObject(this.vaultContent);
      } finally {
         this.safeClose((OutputStream)oos);
         this.safeClose((OutputStream)fos);
      }

   }

   private boolean vaultFileExists(String fileName) {
      File file = new File(this.decodedEncFileDir + fileName);
      return file != null && file.exists();
   }

   private boolean directoryExists(String dir) {
      File file = new File(dir);
      return file != null && file.exists();
   }

   private void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }

   private void safeClose(OutputStream os) {
      try {
         if (os != null) {
            os.close();
         }
      } catch (Exception var3) {
      }

   }

   private void readVaultContent(String keystoreURL, String encFileDir) throws SecurityVaultException {
      try {
         if (encFileDir.contains("${)")) {
            encFileDir = encFileDir.replaceAll(":", "::");
         }

         this.decodedEncFileDir = StringUtil.getSystemPropertyAsString(encFileDir);
         if (!this.directoryExists(this.decodedEncFileDir)) {
            throw new SecurityVaultException(PicketBoxMessages.MESSAGES.fileOrDirectoryDoesNotExistMessage(this.decodedEncFileDir));
         } else {
            if (!this.decodedEncFileDir.endsWith("/") && !this.decodedEncFileDir.endsWith("\\")) {
               this.decodedEncFileDir = this.decodedEncFileDir + File.separator;
            }

            if (this.vaultFileExists("ENC.dat")) {
               if (this.vaultFileExists("VAULT.dat")) {
                  PicketBoxLogger.LOGGER.mixedVaultDataFound("VAULT.dat", "ENC.dat", this.decodedEncFileDir + "ENC.dat");
                  throw PicketBoxMessages.MESSAGES.mixedVaultDataFound("VAULT.dat", "ENC.dat");
               }

               this.convertVaultContent(keystoreURL, this.alias);
            } else if (this.vaultFileExists("VAULT.dat")) {
               this.readVersionedVaultContent();
            } else {
               this.setUpVault(keystoreURL, this.decodedEncFileDir);
            }

         }
      } catch (Exception var4) {
         throw new SecurityVaultException(var4);
      }
   }

   private void convertVaultContent(String keystoreURL, String alias) throws Exception {
      FileInputStream fis = null;
      ObjectInputStream ois = null;

      Map theContent;
      try {
         fis = new FileInputStream(this.decodedEncFileDir + "ENC.dat");
         ois = new ObjectInputStream(fis);
         theContent = (Map)ois.readObject();
      } finally {
         this.safeClose((InputStream)fis);
         this.safeClose((InputStream)ois);
      }

      this.vaultContent = new SecurityVaultData();
      this.adminKey = null;
      Iterator i$ = theContent.keySet().iterator();

      String vaultBlock;
      while(i$.hasNext()) {
         String key = (String)i$.next();
         if (key.equals("ADMIN_KEY")) {
            byte[] admin_key = (byte[])theContent.get(key);
            this.adminKey = new SecretKeySpec(admin_key, this.encryptionAlgorithm);
         } else if (key.contains("_")) {
            StringTokenizer tokenizer = new StringTokenizer(key, "_");
            vaultBlock = tokenizer.nextToken();
            String attributeName = tokenizer.nextToken();
            if (tokenizer.hasMoreTokens()) {
               attributeName = key.substring(vaultBlock.length() + 1);
               PicketBoxLogger.LOGGER.ambiguosKeyForSecurityVaultTransformation("_", vaultBlock, attributeName);
            }

            byte[] encodedAttributeValue = (byte[])theContent.get(key);
            this.vaultContent.addVaultData(alias, vaultBlock, attributeName, encodedAttributeValue);
         }
      }

      if (this.adminKey == null) {
         throw PicketBoxMessages.MESSAGES.missingAdminKeyInOriginalVaultData();
      } else {
         SecretKeyEntry skEntry = new SecretKeyEntry(this.adminKey);
         PasswordProtection p = new PasswordProtection(this.keyStorePWD);
         Entry e = this.keystore.getEntry(alias, p);
         if (e != null) {
            vaultBlock = alias + "-original";
            this.keystore.setEntry(vaultBlock, e, p);
            this.keystore.deleteEntry(alias);
         }

         this.keystore.setEntry(alias, skEntry, new PasswordProtection(this.keyStorePWD));
         this.saveKeyStoreToFile(keystoreURL);
         copyFile(new File(this.decodedEncFileDir + "ENC.dat"), new File(this.decodedEncFileDir + "ENC.dat" + ".original"));
         this.writeVaultData();
         File f = new File(this.decodedEncFileDir + "ENC.dat");
         if (!f.delete()) {
            PicketBoxLogger.LOGGER.cannotDeleteOriginalVaultFile(f.getCanonicalPath());
         }

         f = new File(this.decodedEncFileDir + "Shared.dat");
         if (!f.delete()) {
            PicketBoxLogger.LOGGER.cannotDeleteOriginalVaultFile(f.getCanonicalPath());
         }

      }
   }

   private void saveKeyStoreToFile(String keystoreURL) throws Exception {
      this.keystore.store(new FileOutputStream(new File(keystoreURL)), this.keyStorePWD);
   }

   private void checkAndConvertKeyStoreToJCEKS(String keystoreURL) throws Exception {
      if (this.keystore.getType().equalsIgnoreCase("JKS")) {
         copyFile(new File(keystoreURL), new File(keystoreURL + ".original"));
         KeyStore jceks = KeyStoreUtil.createKeyStore("JCEKS", this.keyStorePWD);
         Enumeration aliases = this.keystore.aliases();

         while(aliases.hasMoreElements()) {
            String entryAlias = (String)aliases.nextElement();
            PasswordProtection p = new PasswordProtection(this.keyStorePWD);
            Entry e = this.keystore.getEntry(entryAlias, p);
            jceks.setEntry(entryAlias, e, p);
         }

         this.keystore = jceks;
         this.keyStoreType = "JCEKS";
         this.saveKeyStoreToFile(keystoreURL);
         PicketBoxLogger.LOGGER.keyStoreConvertedToJCEKS("KEYSTORE_URL");
      }

   }

   private void readVersionedVaultContent() throws Exception {
      FileInputStream fis = null;
      ObjectInputStream ois = null;

      try {
         fis = new FileInputStream(this.decodedEncFileDir + "VAULT.dat");
         ois = new ObjectInputStream(fis);
         this.vaultContent = (SecurityVaultData)ois.readObject();
      } finally {
         this.safeClose((InputStream)fis);
         this.safeClose((InputStream)ois);
      }

      this.adminKey = this.getAdminKey();
      if (this.adminKey == null) {
         throw PicketBoxMessages.MESSAGES.vaultDoesnotContainSecretKey(this.alias);
      }
   }

   private SecretKey getAdminKey() {
      try {
         Entry e = this.keystore.getEntry(this.alias, new PasswordProtection(this.keyStorePWD));
         return e instanceof SecretKeyEntry ? ((SecretKeyEntry)e).getSecretKey() : null;
      } catch (Exception var2) {
         PicketBoxLogger.LOGGER.vaultDoesnotContainSecretKey(this.alias);
         return null;
      }
   }

   public static void copyFile(File sourceFile, File destFile) throws IOException {
      if (!destFile.exists()) {
         destFile.createNewFile();
      }

      FileInputStream fIn = null;
      FileOutputStream fOut = null;
      FileChannel source = null;
      FileChannel destination = null;

      try {
         fIn = new FileInputStream(sourceFile);
         source = fIn.getChannel();
         fOut = new FileOutputStream(destFile);
         destination = fOut.getChannel();
         long transfered = 0L;
         long bytes = source.size();

         while(transfered < bytes) {
            transfered += destination.transferFrom(source, 0L, source.size());
            destination.position(transfered);
         }
      } finally {
         if (source != null) {
            source.close();
         } else if (fIn != null) {
            fIn.close();
         }

         if (destination != null) {
            destination.close();
         } else if (fOut != null) {
            fOut.close();
         }

      }

   }

   private KeyStore getKeyStore(String keystoreURL) {
      try {
         if (this.createKeyStore) {
            return KeyStoreUtil.createKeyStore(this.keyStoreType, this.keyStorePWD);
         }
      } catch (Throwable var5) {
         throw PicketBoxMessages.MESSAGES.unableToGetKeyStore(var5, keystoreURL);
      }

      try {
         return KeyStoreUtil.getKeyStore(this.keyStoreType, keystoreURL, this.keyStorePWD);
      } catch (IOException var3) {
         throw PicketBoxMessages.MESSAGES.unableToGetKeyStore(var3, keystoreURL);
      } catch (GeneralSecurityException var4) {
         throw PicketBoxMessages.MESSAGES.unableToGetKeyStore(var4, keystoreURL);
      }
   }
}
