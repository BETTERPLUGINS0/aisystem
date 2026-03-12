package fr.xephi.authme.libs.org.jboss.security;

import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Properties;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

public interface JSSESecurityDomain extends BaseSecurityManager {
   KeyStore getKeyStore() throws SecurityException;

   KeyManager[] getKeyManagers() throws SecurityException;

   KeyStore getTrustStore() throws SecurityException;

   TrustManager[] getTrustManagers() throws SecurityException;

   void reloadKeyAndTrustStore() throws Exception;

   String getServerAlias();

   String getClientAlias();

   boolean isClientAuth();

   Key getKey(String var1, String var2) throws Exception;

   Certificate getCertificate(String var1) throws Exception;

   String[] getCipherSuites();

   String[] getProtocols();

   Properties getAdditionalProperties();
}
