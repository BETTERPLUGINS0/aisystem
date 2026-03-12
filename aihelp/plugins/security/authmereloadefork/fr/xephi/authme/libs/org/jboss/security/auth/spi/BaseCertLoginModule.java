package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.JSSESecurityDomain;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityDomain;
import fr.xephi.authme.libs.org.jboss.security.SecurityUtil;
import fr.xephi.authme.libs.org.jboss.security.auth.callback.ObjectCallback;
import fr.xephi.authme.libs.org.jboss.security.auth.certs.X509CertificateVerifier;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.acl.Group;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;

public class BaseCertLoginModule extends AbstractServerLoginModule {
   private static final String SECURITY_DOMAIN = "securityDomain";
   private static final String VERIFIER = "verifier";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"securityDomain", "verifier"};
   private Principal identity;
   private X509Certificate credential;
   private Object domain = null;
   private X509CertificateVerifier verifier;

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      String sd = (String)options.get("securityDomain");
      sd = SecurityUtil.unprefixSecurityDomain(sd);
      if (sd == null) {
         sd = "other";
      }

      try {
         Object tempDomain = (new InitialContext()).lookup("java:jboss/jaas/" + sd);
         if (tempDomain instanceof SecurityDomain) {
            this.domain = tempDomain;
            PicketBoxLogger.LOGGER.traceSecurityDomainFound(this.domain.getClass().getName());
         } else {
            tempDomain = (new InitialContext()).lookup("java:jboss/jaas/" + sd + "/jsse");
            if (tempDomain instanceof JSSESecurityDomain) {
               this.domain = tempDomain;
               PicketBoxLogger.LOGGER.traceSecurityDomainFound(this.domain.getClass().getName());
            } else {
               PicketBoxLogger.LOGGER.errorGettingJSSESecurityDomain(sd);
            }
         }
      } catch (NamingException var10) {
         PicketBoxLogger.LOGGER.errorFindingSecurityDomain(sd, var10);
      }

      String option = (String)options.get("verifier");
      if (option != null) {
         try {
            ClassLoader loader = SecurityActions.getContextClassLoader();
            Class<?> verifierClass = loader.loadClass(option);
            this.verifier = (X509CertificateVerifier)verifierClass.newInstance();
         } catch (Throwable var9) {
            PicketBoxLogger.LOGGER.errorCreatingCertificateVerifier(var9);
         }
      }

      PicketBoxLogger.LOGGER.traceEndInitialize();
   }

   public boolean login() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginLogin();
      String alias;
      if (super.login()) {
         Object username = this.sharedState.get("javax.security.auth.login.name");
         if (username instanceof Principal) {
            this.identity = (Principal)username;
         } else {
            alias = username.toString();

            try {
               this.identity = this.createIdentity(alias);
            } catch (Exception var4) {
               throw PicketBoxMessages.MESSAGES.failedToCreatePrincipal(var4.getLocalizedMessage());
            }
         }

         Object password = this.sharedState.get("javax.security.auth.login.password");
         if (password instanceof X509Certificate) {
            this.credential = (X509Certificate)password;
         } else if (password != null) {
            PicketBoxLogger.LOGGER.debugPasswordNotACertificate();
            super.loginOk = false;
            return false;
         }

         return true;
      } else {
         super.loginOk = false;
         Object[] info = this.getAliasAndCert();
         alias = (String)info[0];
         this.credential = (X509Certificate)info[1];
         if (alias == null && this.credential == null) {
            this.identity = this.unauthenticatedIdentity;
            PicketBoxLogger.LOGGER.traceUsingUnauthIdentity(this.identity.toString());
         }

         if (this.identity == null) {
            try {
               this.identity = this.createIdentity(alias);
            } catch (Exception var5) {
               PicketBoxLogger.LOGGER.debugFailureToCreateIdentityForAlias(alias, var5);
            }

            if (!this.validateCredential(alias, this.credential)) {
               throw PicketBoxMessages.MESSAGES.failedToMatchCredential(alias);
            }
         }

         if (this.getUseFirstPass()) {
            this.sharedState.put("javax.security.auth.login.name", alias);
            this.sharedState.put("javax.security.auth.login.password", this.credential);
         }

         super.loginOk = true;
         PicketBoxLogger.LOGGER.traceEndLogin(super.loginOk);
         return true;
      }
   }

   public boolean commit() throws LoginException {
      boolean ok = super.commit();
      if (ok && this.credential != null) {
         this.subject.getPublicCredentials().add(this.credential);
      }

      return ok;
   }

   protected Group[] getRoleSets() throws LoginException {
      return new Group[0];
   }

   protected Principal getIdentity() {
      return this.identity;
   }

   protected Object getCredentials() {
      return this.credential;
   }

   protected String getUsername() {
      String username = null;
      if (this.getIdentity() != null) {
         username = this.getIdentity().getName();
      }

      return username;
   }

   protected Object[] getAliasAndCert() throws LoginException {
      PicketBoxLogger.LOGGER.traceBeginGetAliasAndCert();
      Object[] info = new Object[]{null, null};
      if (this.callbackHandler == null) {
         throw PicketBoxMessages.MESSAGES.noCallbackHandlerAvailable();
      } else {
         NameCallback nc = new NameCallback("Alias: ");
         ObjectCallback oc = new ObjectCallback("Certificate: ");
         Callback[] callbacks = new Callback[]{nc, oc};
         String alias = null;
         X509Certificate cert = null;

         LoginException le;
         try {
            this.callbackHandler.handle(callbacks);
            alias = nc.getName();
            Object tmpCert = oc.getCredential();
            if (tmpCert != null) {
               if (tmpCert instanceof X509Certificate) {
                  cert = (X509Certificate)tmpCert;
                  PicketBoxLogger.LOGGER.traceCertificateFound(cert.getSerialNumber().toString(16), cert.getSubjectDN().getName());
               } else {
                  if (!(tmpCert instanceof X509Certificate[])) {
                     throw PicketBoxMessages.MESSAGES.unableToGetCertificateFromClass(tmpCert != null ? tmpCert.getClass() : null);
                  }

                  X509Certificate[] certChain = (X509Certificate[])((X509Certificate[])tmpCert);
                  if (certChain.length > 0) {
                     cert = certChain[0];
                  }
               }
            } else {
               PicketBoxLogger.LOGGER.warnNullCredentialFromCallbackHandler();
            }
         } catch (IOException var10) {
            le = PicketBoxMessages.MESSAGES.failedToInvokeCallbackHandler();
            le.initCause(var10);
            throw le;
         } catch (UnsupportedCallbackException var11) {
            le = new LoginException();
            le.initCause(var11);
            throw le;
         }

         info[0] = alias;
         info[1] = cert;
         PicketBoxLogger.LOGGER.traceEndGetAliasAndCert();
         return info;
      }
   }

   protected boolean validateCredential(String alias, X509Certificate cert) {
      PicketBoxLogger.LOGGER.traceBeginValidateCredential();
      boolean isValid = false;
      KeyStore keyStore = null;
      KeyStore trustStore = null;
      if (this.domain != null) {
         if (this.domain instanceof SecurityDomain) {
            keyStore = ((SecurityDomain)this.domain).getKeyStore();
            trustStore = ((SecurityDomain)this.domain).getTrustStore();
         } else if (this.domain instanceof JSSESecurityDomain) {
            keyStore = ((JSSESecurityDomain)this.domain).getKeyStore();
            trustStore = ((JSSESecurityDomain)this.domain).getTrustStore();
         }
      }

      if (trustStore == null) {
         trustStore = keyStore;
      }

      if (this.verifier != null) {
         PicketBoxLogger.LOGGER.traceValidatingUsingVerifier(this.verifier.getClass());
         isValid = this.verifier.verify(cert, alias, keyStore, trustStore);
      } else if (trustStore != null && cert != null) {
         X509Certificate storeCert = null;

         try {
            storeCert = (X509Certificate)trustStore.getCertificate(alias);
            if (PicketBoxLogger.LOGGER.isTraceEnabled()) {
               StringBuffer buf = new StringBuffer("\n\t");
               buf.append(PicketBoxMessages.MESSAGES.suppliedCredentialMessage());
               buf.append(cert.getSerialNumber().toString(16));
               buf.append("\n\t\t");
               buf.append(cert.getSubjectDN().getName());
               buf.append("\n\n\t");
               buf.append(PicketBoxMessages.MESSAGES.existingCredentialMessage());
               if (storeCert != null) {
                  buf.append(storeCert.getSerialNumber().toString(16));
                  buf.append("\n\t\t");
                  buf.append(storeCert.getSubjectDN().getName());
                  buf.append("\n");
               } else {
                  ArrayList<String> aliases = new ArrayList();
                  Enumeration en = trustStore.aliases();

                  while(en.hasMoreElements()) {
                     aliases.add(en.nextElement());
                  }

                  buf.append(PicketBoxMessages.MESSAGES.noMatchForAliasMessage(alias, aliases));
               }

               PicketBoxLogger.LOGGER.trace(buf.toString());
            }
         } catch (KeyStoreException var10) {
            PicketBoxLogger.LOGGER.warnFailureToFindCertForAlias(alias, var10);
         }

         if (cert.equals(storeCert)) {
            isValid = true;
         }
      } else {
         PicketBoxLogger.LOGGER.warnFailureToValidateCertificate();
      }

      PicketBoxLogger.LOGGER.traceEndValidateCredential(isValid);
      return isValid;
   }
}
