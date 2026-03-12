package fr.xephi.authme.libs.org.mariadb.jdbc.client.tls;

import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.security.auth.x500.X500Principal;

public class MariaDbX509KeyManager extends X509ExtendedKeyManager {
   private final Hashtable<String, PrivateKeyEntry> privateKeyHash = new Hashtable();

   public MariaDbX509KeyManager(KeyStore keyStore, char[] pwd) throws KeyStoreException {
      Enumeration aliases = keyStore.aliases();

      while(aliases.hasMoreElements()) {
         String alias = (String)aliases.nextElement();
         if (keyStore.entryInstanceOf(alias, PrivateKeyEntry.class)) {
            try {
               this.privateKeyHash.put(alias, (PrivateKeyEntry)keyStore.getEntry(alias, new PasswordProtection(pwd)));
            } catch (NoSuchAlgorithmException | UnrecoverableEntryException var6) {
            }
         }
      }

   }

   public String[] getClientAliases(String keyType, Principal[] issuers) {
      List<String> accurateAlias = this.searchAccurateAliases(new String[]{keyType}, issuers);
      return accurateAlias.size() == 0 ? null : (String[])accurateAlias.toArray(new String[0]);
   }

   public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
      List<String> accurateAlias = this.searchAccurateAliases(keyType, issuers);
      return accurateAlias != null && !accurateAlias.isEmpty() ? (String)accurateAlias.get(0) : null;
   }

   public X509Certificate[] getCertificateChain(String alias) {
      PrivateKeyEntry keyEntry = (PrivateKeyEntry)this.privateKeyHash.get(alias);
      if (keyEntry == null) {
         return null;
      } else {
         Certificate[] certs = keyEntry.getCertificateChain();
         return certs.length > 0 && certs[0] instanceof X509Certificate ? (X509Certificate[])Arrays.copyOf(certs, certs.length, X509Certificate[].class) : null;
      }
   }

   public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine) {
      return this.chooseClientAlias(keyType, issuers, (Socket)null);
   }

   public PrivateKey getPrivateKey(String alias) {
      PrivateKeyEntry keyEntry = (PrivateKeyEntry)this.privateKeyHash.get(alias);
      return keyEntry == null ? null : keyEntry.getPrivateKey();
   }

   private ArrayList<String> searchAccurateAliases(String[] keyTypes, Principal[] issuers) {
      if (keyTypes != null && keyTypes.length != 0) {
         ArrayList<String> accurateAliases = new ArrayList();
         Iterator var4 = this.privateKeyHash.entrySet().iterator();

         while(var4.hasNext()) {
            Entry<String, PrivateKeyEntry> mapEntry = (Entry)var4.next();
            Certificate[] certs = ((PrivateKeyEntry)mapEntry.getValue()).getCertificateChain();
            String alg = certs[0].getPublicKey().getAlgorithm();
            String[] var8 = keyTypes;
            int var9 = keyTypes.length;

            label54:
            for(int var10 = 0; var10 < var9; ++var10) {
               String keyType = var8[var10];
               if (alg.equals(keyType)) {
                  if (issuers != null && issuers.length != 0) {
                     Certificate[] var12 = certs;
                     int var13 = certs.length;

                     for(int var14 = 0; var14 < var13; ++var14) {
                        Certificate cert = var12[var14];
                        if (cert instanceof X509Certificate) {
                           X500Principal certificateIssuer = ((X509Certificate)cert).getIssuerX500Principal();
                           Principal[] var17 = issuers;
                           int var18 = issuers.length;

                           for(int var19 = 0; var19 < var18; ++var19) {
                              Principal issuer = var17[var19];
                              if (certificateIssuer.equals(issuer)) {
                                 accurateAliases.add((String)mapEntry.getKey());
                                 continue label54;
                              }
                           }
                        }
                     }
                  } else {
                     accurateAliases.add((String)mapEntry.getKey());
                  }
               }
            }
         }

         return accurateAliases;
      } else {
         return null;
      }
   }

   public String[] getServerAliases(String keyType, Principal[] issuers) {
      return null;
   }

   public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
      return null;
   }

   public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
      return null;
   }
}
