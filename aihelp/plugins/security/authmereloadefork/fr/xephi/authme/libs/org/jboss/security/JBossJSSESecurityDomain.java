package fr.xephi.authme.libs.org.jboss.security;

import fr.xephi.authme.libs.org.jboss.security.plugins.SecurityKeyManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.Provider;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Properties;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;

public class JBossJSSESecurityDomain implements JSSESecurityDomain {
   private KeyStore keyStore;
   private KeyManagerFactory keyManagerFactory;
   private KeyManager[] keyManagers;
   private String keyStoreType = "JKS";
   private URL keyStoreURL;
   private char[] keyStorePassword;
   private String keyStoreProvider;
   private String keyStoreProviderArgument;
   private String keyManagerFactoryProvider;
   private String keyManagerFactoryAlgorithm;
   private KeyStore trustStore;
   private TrustManagerFactory trustManagerFactory;
   private TrustManager[] trustManagers;
   private String trustStoreType = "JKS";
   private URL trustStoreURL;
   private char[] trustStorePassword;
   private String trustStoreProvider;
   private String trustStoreProviderArgument;
   private String trustManagerFactoryProvider;
   private String trustManagerFactoryAlgorithm;
   private String clientAlias;
   private String serverAlias;
   private boolean clientAuth;
   private char[] serviceAuthToken;
   private String[] cipherSuites;
   private String[] protocols;
   private Properties additionalProperties;
   private String name;

   public JBossJSSESecurityDomain(String securityDomainName) {
      this.name = securityDomainName;
   }

   public String getKeyStoreType() {
      return this.keyStoreType;
   }

   public void setKeyStoreType(String keyStoreType) {
      this.keyStoreType = keyStoreType;
   }

   public String getKeyStoreURL() {
      String url = null;
      if (this.keyStoreURL != null) {
         url = this.keyStoreURL.toExternalForm();
      }

      return url;
   }

   public void setKeyStoreURL(String keyStoreURL) throws IOException {
      this.keyStoreURL = this.validateStoreURL(keyStoreURL);
   }

   public String getKeyStoreProvider() {
      return this.keyStoreProvider;
   }

   public void setKeyStoreProvider(String keyStoreProvider) {
      this.keyStoreProvider = keyStoreProvider;
   }

   public String getKeyManagerFactoryProvider() {
      return this.keyManagerFactoryProvider;
   }

   public String getKeyStoreProviderArgument() {
      return this.keyStoreProviderArgument;
   }

   public void setKeyStoreProviderArgument(String keyStoreProviderArgument) {
      this.keyStoreProviderArgument = keyStoreProviderArgument;
   }

   public void setKeyManagerFactoryProvider(String keyManagerFactoryProvider) {
      this.keyManagerFactoryProvider = keyManagerFactoryProvider;
   }

   public String getKeyManagerFactoryAlgorithm() {
      return this.keyManagerFactoryAlgorithm;
   }

   public void setKeyManagerFactoryAlgorithm(String keyManagerFactoryAlgorithm) {
      this.keyManagerFactoryAlgorithm = keyManagerFactoryAlgorithm;
   }

   public String getTrustStoreType() {
      return this.trustStoreType;
   }

   public void setTrustStoreType(String trustStoreType) {
      this.trustStoreType = trustStoreType;
   }

   public String getTrustStoreURL() {
      String url = null;
      if (this.trustStoreURL != null) {
         url = this.trustStoreURL.toExternalForm();
      }

      return url;
   }

   public void setTrustStoreURL(String trustStoreURL) throws IOException {
      this.trustStoreURL = this.validateStoreURL(trustStoreURL);
   }

   public String getTrustStoreProvider() {
      return this.trustStoreProvider;
   }

   public void setTrustStoreProvider(String trustStoreProvider) {
      this.trustStoreProvider = trustStoreProvider;
   }

   public String getTrustStoreProviderArgument() {
      return this.trustStoreProviderArgument;
   }

   public void setTrustStoreProviderArgument(String trustStoreProviderArgument) {
      this.trustStoreProviderArgument = trustStoreProviderArgument;
   }

   public String getTrustManagerFactoryProvider() {
      return this.trustManagerFactoryProvider;
   }

   public void setTrustManagerFactoryProvider(String trustManagerFactoryProvider) {
      this.trustManagerFactoryProvider = trustManagerFactoryProvider;
   }

   public String getTrustManagerFactoryAlgorithm() {
      return this.trustManagerFactoryAlgorithm;
   }

   public void setTrustManagerFactoryAlgorithm(String trustManagerFactoryAlgorithm) {
      this.trustManagerFactoryAlgorithm = trustManagerFactoryAlgorithm;
   }

   public String getClientAlias() {
      return this.clientAlias;
   }

   public void setClientAlias(String clientAlias) {
      this.clientAlias = clientAlias;
   }

   public String getServerAlias() {
      return this.serverAlias;
   }

   public void setServerAlias(String serverAlias) {
      this.serverAlias = serverAlias;
   }

   public boolean isClientAuth() {
      return this.clientAuth;
   }

   public void setClientAuth(boolean clientAuth) {
      this.clientAuth = clientAuth;
   }

   public KeyStore getKeyStore() {
      return this.keyStore;
   }

   public KeyStore getTrustStore() {
      return this.trustStore;
   }

   public void setKeyStorePassword(String keyStorePassword) throws Exception {
      this.keyStorePassword = Util.loadPassword(keyStorePassword);
   }

   public void setTrustStorePassword(String trustStorePassword) throws Exception {
      this.trustStorePassword = Util.loadPassword(trustStorePassword);
   }

   public void setServiceAuthToken(String serviceAuthToken) throws Exception {
      this.serviceAuthToken = Util.loadPassword(serviceAuthToken);
   }

   public KeyManager[] getKeyManagers() throws SecurityException {
      return this.keyManagers;
   }

   public TrustManager[] getTrustManagers() throws SecurityException {
      return this.trustManagers;
   }

   public String getSecurityDomain() {
      return this.name;
   }

   public Key getKey(String alias, String serviceAuthToken) throws Exception {
      PicketBoxLogger.LOGGER.traceJSSEDomainGetKey(alias);
      Key key = this.keyStore.getKey(alias, this.keyStorePassword);
      if (key != null && !(key instanceof PublicKey)) {
         this.verifyServiceAuthToken(serviceAuthToken);
         return key;
      } else {
         return key;
      }
   }

   public Certificate getCertificate(String alias) throws Exception {
      PicketBoxLogger.LOGGER.traceJSSEDomainGetCertificate(alias);
      return this.trustStore.getCertificate(alias);
   }

   public void reloadKeyAndTrustStore() throws Exception {
      this.loadKeyAndTrustStore();
   }

   public String[] getCipherSuites() {
      return this.cipherSuites;
   }

   public void setCipherSuites(String cipherSuites) {
      String[] cs = cipherSuites.split(",");
      this.cipherSuites = cs;
   }

   public String[] getProtocols() {
      return this.protocols;
   }

   public void setProtocols(String protocols) {
      String[] p = protocols.split(",");
      this.protocols = p;
   }

   public Properties getAdditionalProperties() {
      return this.additionalProperties;
   }

   public void setAdditionalProperties(Properties properties) {
      this.additionalProperties = properties;
   }

   private URL validateStoreURL(String storeURL) throws IOException {
      URL url = null;

      try {
         url = new URL(storeURL);
      } catch (MalformedURLException var4) {
      }

      if (url == null) {
         File tst = new File(storeURL);
         if (tst.exists()) {
            url = tst.toURI().toURL();
         }
      }

      if (url == null) {
         ClassLoader loader = SecurityActions.getContextClassLoader();
         if (loader != null) {
            url = loader.getResource(storeURL);
         }
      }

      if (url == null) {
         throw PicketBoxMessages.MESSAGES.failedToValidateURL(storeURL);
      } else {
         return url;
      }
   }

   private void verifyServiceAuthToken(String serviceAuthToken) throws SecurityException {
      if (this.serviceAuthToken == null) {
         throw PicketBoxMessages.MESSAGES.missingServiceAuthToken(this.getSecurityDomain());
      } else {
         boolean verificationSuccessful = true;
         char[] ca = serviceAuthToken.toCharArray();
         if (this.serviceAuthToken.length == ca.length) {
            for(int i = 0; i < this.serviceAuthToken.length; ++i) {
               if (this.serviceAuthToken[i] != ca[i]) {
                  verificationSuccessful = false;
                  break;
               }
            }

            if (verificationSuccessful) {
               return;
            }
         }

         throw PicketBoxMessages.MESSAGES.failedToVerifyServiceAuthToken();
      }
   }

   private void loadKeyAndTrustStore() throws Exception {
      InputStream is = null;
      String algorithm;
      ClassLoader loader;
      Class clazz;
      Class[] ctorSig;
      Constructor ctor;
      Object[] ctorArgs;
      Provider provider;
      if (this.keyStorePassword != null) {
         if (this.keyStoreProvider != null) {
            if (this.keyStoreProviderArgument != null) {
               loader = SecurityActions.getContextClassLoader();
               clazz = loader.loadClass(this.keyStoreProvider);
               ctorSig = new Class[]{String.class};
               ctor = clazz.getConstructor(ctorSig);
               ctorArgs = new Object[]{this.keyStoreProviderArgument};
               provider = (Provider)ctor.newInstance(ctorArgs);
               this.keyStore = KeyStore.getInstance(this.keyStoreType, provider);
            } else {
               this.keyStore = KeyStore.getInstance(this.keyStoreType, this.keyStoreProvider);
            }
         } else {
            this.keyStore = KeyStore.getInstance(this.keyStoreType);
         }

         is = null;

         try {
            if (!"PKCS11".equalsIgnoreCase(this.keyStoreType) && !"PKCS11IMPLKS".equalsIgnoreCase(this.keyStoreType)) {
               if (this.keyStoreURL == null) {
                  throw PicketBoxMessages.MESSAGES.invalidNullKeyStoreURL(this.keyStoreType);
               }

               is = this.keyStoreURL.openStream();
            }

            this.keyStore.load(is, this.keyStorePassword);
         } finally {
            this.safeClose(is);
         }

         loader = null;
         if (this.keyManagerFactoryAlgorithm != null) {
            algorithm = this.keyManagerFactoryAlgorithm;
         } else {
            algorithm = KeyManagerFactory.getDefaultAlgorithm();
         }

         if (this.keyManagerFactoryProvider != null) {
            this.keyManagerFactory = KeyManagerFactory.getInstance(algorithm, this.keyManagerFactoryProvider);
         } else {
            this.keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
         }

         this.keyManagerFactory.init(this.keyStore, this.keyStorePassword);
         this.keyManagers = this.keyManagerFactory.getKeyManagers();

         for(int i = 0; i < this.keyManagers.length; ++i) {
            this.keyManagers[i] = new SecurityKeyManager((X509KeyManager)this.keyManagers[i], this.serverAlias, this.clientAlias);
         }
      }

      if (this.trustStorePassword != null) {
         if (this.trustStoreProvider != null) {
            if (this.trustStoreProviderArgument != null) {
               loader = Thread.currentThread().getContextClassLoader();
               clazz = loader.loadClass(this.trustStoreProvider);
               ctorSig = new Class[]{String.class};
               ctor = clazz.getConstructor(ctorSig);
               ctorArgs = new Object[]{this.trustStoreProviderArgument};
               provider = (Provider)ctor.newInstance(ctorArgs);
               this.trustStore = KeyStore.getInstance(this.trustStoreType, provider);
            } else {
               this.trustStore = KeyStore.getInstance(this.trustStoreType, this.trustStoreProvider);
            }
         } else {
            this.trustStore = KeyStore.getInstance(this.trustStoreType);
         }

         is = null;

         try {
            if (!"PKCS11".equalsIgnoreCase(this.trustStoreType) && !"PKCS11IMPLKS".equalsIgnoreCase(this.trustStoreType)) {
               if (this.trustStoreURL == null) {
                  throw PicketBoxMessages.MESSAGES.invalidNullKeyStoreURL(this.trustStoreType);
               }

               is = this.trustStoreURL.openStream();
            }

            this.trustStore.load(is, this.trustStorePassword);
         } finally {
            this.safeClose(is);
         }

         loader = null;
         if (this.trustManagerFactoryAlgorithm != null) {
            algorithm = this.trustManagerFactoryAlgorithm;
         } else {
            algorithm = TrustManagerFactory.getDefaultAlgorithm();
         }

         if (this.trustManagerFactoryProvider != null) {
            this.trustManagerFactory = TrustManagerFactory.getInstance(algorithm, this.trustManagerFactoryProvider);
         } else {
            this.trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
         }

         this.trustManagerFactory.init(this.trustStore);
         this.trustManagers = this.trustManagerFactory.getTrustManagers();
      } else if (this.keyStore != null) {
         this.trustStore = this.keyStore;
         loader = null;
         if (this.trustManagerFactoryAlgorithm != null) {
            algorithm = this.trustManagerFactoryAlgorithm;
         } else {
            algorithm = TrustManagerFactory.getDefaultAlgorithm();
         }

         this.trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
         this.trustManagerFactory.init(this.trustStore);
         this.trustManagers = this.trustManagerFactory.getTrustManagers();
      }

   }

   private void safeClose(InputStream fis) {
      try {
         if (fis != null) {
            fis.close();
         }
      } catch (Exception var3) {
      }

   }
}
