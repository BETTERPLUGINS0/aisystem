package fr.xephi.authme.libs.org.jboss.security.plugins;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509KeyManager;

public class SecurityKeyManager implements X509KeyManager {
   private X509KeyManager delegate;
   private String serverAlias;
   private String clientAlias;

   public SecurityKeyManager(X509KeyManager keyManager, String serverAlias, String clientAlias) {
      this.delegate = keyManager;
      this.serverAlias = serverAlias;
      this.clientAlias = clientAlias;
   }

   public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
      return this.clientAlias != null ? this.clientAlias : this.delegate.chooseClientAlias(keyType, issuers, socket);
   }

   public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
      return this.serverAlias != null ? this.serverAlias : this.delegate.chooseServerAlias(keyType, issuers, socket);
   }

   public X509Certificate[] getCertificateChain(String alias) {
      return this.delegate.getCertificateChain(alias);
   }

   public String[] getClientAliases(String keyType, Principal[] issuers) {
      return this.delegate.getClientAliases(keyType, issuers);
   }

   public PrivateKey getPrivateKey(String alias) {
      return this.delegate.getPrivateKey(alias);
   }

   public String[] getServerAliases(String keyType, Principal[] issuers) {
      return this.delegate.getServerAliases(keyType, issuers);
   }
}
