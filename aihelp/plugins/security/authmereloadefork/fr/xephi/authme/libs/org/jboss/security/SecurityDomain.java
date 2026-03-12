package fr.xephi.authme.libs.org.jboss.security;

import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

public interface SecurityDomain extends SubjectSecurityManager, RealmMapping {
   KeyStore getKeyStore() throws SecurityException;

   KeyManagerFactory getKeyManagerFactory() throws SecurityException;

   KeyStore getTrustStore() throws SecurityException;

   TrustManagerFactory getTrustManagerFactory() throws SecurityException;
}
