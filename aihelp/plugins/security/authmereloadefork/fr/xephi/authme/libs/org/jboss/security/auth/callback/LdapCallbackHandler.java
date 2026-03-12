package fr.xephi.authme.libs.org.jboss.security.auth.callback;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.Util;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import javax.management.ObjectName;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class LdapCallbackHandler extends AbstractCallbackHandler implements CallbackHandler {
   private static final String PASSWORD_ATTRIBUTE_ID = "passwordAttributeID";
   private static final String BIND_DN = "bindDN";
   private static final String BIND_CREDENTIAL = "bindCredential";
   private static final String BASE_CTX_DN = "baseCtxDN";
   private static final String BASE_FILTER_OPT = "baseFilter";
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
   private static final String SECURITY_DOMAIN_OPT = "jaasSecurityDomain";
   private static final String DISTINGUISHED_NAME_ATTRIBUTE_OPT = "distinguishedNameAttribute";
   protected String bindDN;
   protected String bindCredential;
   protected String passwordAttributeID = "userPassword";
   protected int searchTimeLimit = 10000;
   protected String distinguishedNameAttribute;
   protected boolean isPasswordValidated = false;
   protected Map<String, String> options = new HashMap();

   public void setConfiguration(Map<String, String> config) {
      if (config != null) {
         this.options.putAll(config);
      }

   }

   public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      if (this.userName == null) {
         this.userName = this.getUserName(callbacks);
      }

      for(int i = 0; i < callbacks.length; ++i) {
         Callback callback = callbacks[i];

         try {
            this.handleCallBack(callback);
         } catch (NamingException var5) {
            throw new IOException(var5);
         }
      }

   }

   protected void handleCallBack(Callback c) throws UnsupportedCallbackException, NamingException {
      if (c instanceof VerifyPasswordCallback) {
         this.verifyPassword((VerifyPasswordCallback)c);
      } else if (c instanceof PasswordCallback) {
         PasswordCallback passwdCallback = (PasswordCallback)c;
         String bindDN = this.getBindDN();
         String bindCredential = this.getBindCredential();
         String tmp = (String)this.options.get("passwordAttributeID");
         if (tmp != null && tmp.length() > 0) {
            this.passwordAttributeID = tmp;
         }

         ClassLoader currentTCCL = SecurityActions.getContextClassLoader();

         InitialLdapContext ctx;
         try {
            if (currentTCCL != null) {
               SecurityActions.setContextClassLoader((ClassLoader)null);
            }

            ctx = this.constructInitialLdapContext(bindDN, bindCredential);
         } catch (NamingException var29) {
            throw new RuntimeException(var29);
         }

         String timeLimit = (String)this.options.get("searchTimeLimit");
         if (timeLimit != null) {
            try {
               this.searchTimeLimit = Integer.parseInt(timeLimit);
            } catch (NumberFormatException var26) {
            }
         }

         if (this.searchTimeLimit == 0) {
            this.searchTimeLimit = 10000;
         }

         String baseDN = (String)this.options.get("baseCtxDN");
         String baseFilter = (String)this.options.get("baseFilter");
         SearchControls constraints = new SearchControls();
         constraints.setSearchScope(2);
         constraints.setTimeLimit(this.searchTimeLimit);
         NamingEnumeration<SearchResult> results = null;
         Object[] filterArgs = new Object[]{this.userName};

         try {
            if (baseDN == null) {
               throw PicketBoxMessages.MESSAGES.invalidNullBaseContextDN();
            }

            results = ctx.search(baseDN, baseFilter, filterArgs, constraints);
            if (!results.hasMore()) {
               this.safeClose(results);
               throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
            }

            SearchResult sr = (SearchResult)results.next();
            String name = sr.getName();
            String userDN = null;
            if (!sr.isRelative()) {
               throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);
            }

            userDN = name + "," + baseDN;
            this.safeClose(results);
            filterArgs = new Object[]{this.userName, userDN};
            results = ctx.search(userDN, baseFilter, filterArgs, constraints);

            try {
               while(results.hasMore()) {
                  sr = (SearchResult)results.next();
                  Attributes attributes = sr.getAttributes();
                  NamingEnumeration ne = attributes.getAll();

                  while(ne != null && ne.hasMoreElements()) {
                     Attribute ldapAtt = (Attribute)ne.next();
                     if (this.passwordAttributeID.equalsIgnoreCase(ldapAtt.getID())) {
                        Object thePass = ldapAtt.get();
                        this.setPasswordCallbackValue(thePass, passwdCallback);
                     }
                  }
               }
            } finally {
               this.safeClose(results);
               this.safeClose(ctx);
               if (currentTCCL != null) {
                  SecurityActions.setContextClassLoader(currentTCCL);
               }

            }
         } catch (NamingException var28) {
            PicketBoxLogger.LOGGER.error(var28);
         }

      }
   }

   protected void verifyPassword(VerifyPasswordCallback vpc) throws NamingException {
      String credential = vpc.getValue();
      ClassLoader currentTCCL = SecurityActions.getContextClassLoader();
      if (currentTCCL != null) {
         SecurityActions.setContextClassLoader((ClassLoader)null);
      }

      String baseDN = (String)this.options.get("baseCtxDN");
      String baseFilter = (String)this.options.get("baseFilter");
      InitialLdapContext ctx = this.constructInitialLdapContext(this.bindDN, this.bindCredential);
      this.bindDNAuthentication(ctx, this.userName, credential, baseDN, baseFilter);
      vpc.setVerified(true);
   }

   protected String getBindDN() {
      String bindDN = (String)this.options.get("bindDN");
      if (bindDN == null || bindDN.length() == 0) {
         PicketBoxLogger.LOGGER.traceBindDNNotFound();
      }

      return bindDN;
   }

   protected String getBindCredential() {
      String bindCredential = (String)this.options.get("bindCredential");
      if (Util.isPasswordCommand(bindCredential)) {
         try {
            bindCredential = new String(Util.loadPassword(bindCredential));
         } catch (Exception var6) {
            PicketBoxLogger.LOGGER.errorDecryptingBindCredential(var6);
         }
      }

      String securityDomain = (String)this.options.get("jaasSecurityDomain");
      if (securityDomain != null) {
         try {
            ObjectName serviceName = new ObjectName(securityDomain);
            char[] tmp = DecodeAction.decode(bindCredential, serviceName);
            bindCredential = new String(tmp);
         } catch (Exception var5) {
            PicketBoxLogger.LOGGER.errorDecryptingBindCredential(var5);
         }
      }

      return bindCredential;
   }

   protected void setPasswordCallbackValue(Object thePass, PasswordCallback passwdCallback) {
      if (thePass instanceof String) {
         String tmp = (String)thePass;
         passwdCallback.setPassword(tmp.toCharArray());
      } else if (thePass instanceof char[]) {
         passwdCallback.setPassword((char[])((char[])thePass));
      } else {
         if (!(thePass instanceof byte[])) {
            throw PicketBoxMessages.MESSAGES.invalidPasswordType(thePass != null ? thePass.getClass() : null);
         }

         byte[] theBytes = (byte[])((byte[])thePass);
         passwdCallback.setPassword((new String(theBytes)).toCharArray());
      }

   }

   private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException {
      Properties env = new Properties();
      Iterator i$ = this.options.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, String> entry = (Entry)i$.next();
         env.put(entry.getKey(), entry.getValue());
      }

      String factoryName = env.getProperty("java.naming.factory.initial");
      if (factoryName == null) {
         factoryName = "com.sun.jndi.ldap.LdapCtxFactory";
         env.setProperty("java.naming.factory.initial", factoryName);
      }

      String authType = env.getProperty("java.naming.security.authentication");
      if (authType == null) {
         env.setProperty("java.naming.security.authentication", "simple");
      }

      String protocol = env.getProperty("java.naming.security.protocol");
      String providerURL = (String)this.options.get("java.naming.provider.url");
      if (providerURL == null) {
         providerURL = "ldap://localhost:" + (protocol != null && protocol.equals("ssl") ? "636" : "389");
      }

      env.setProperty("java.naming.provider.url", providerURL);
      this.distinguishedNameAttribute = (String)this.options.get("distinguishedNameAttribute");
      if (this.distinguishedNameAttribute == null) {
         this.distinguishedNameAttribute = "distinguishedName";
      }

      if (dn != null) {
         env.setProperty("java.naming.security.principal", dn);
      }

      if (credential != null) {
         env.put("java.naming.security.credentials", credential);
      }

      this.traceLDAPEnv(env);
      return new InitialLdapContext(env, (Control[])null);
   }

   protected String bindDNAuthentication(InitialLdapContext ctx, String user, Object credential, String baseDN, String filter) throws NamingException {
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(2);
      constraints.setTimeLimit(this.searchTimeLimit);
      String[] attrList = new String[]{this.distinguishedNameAttribute};
      constraints.setReturningAttributes(attrList);
      NamingEnumeration results = null;
      Object[] filterArgs = new Object[]{user};
      results = ctx.search(baseDN, filter, filterArgs, constraints);
      if (!results.hasMore()) {
         results.close();
         throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
      } else {
         SearchResult sr = (SearchResult)results.next();
         String name = sr.getName();
         String userDN = null;
         Attributes attrs = sr.getAttributes();
         if (attrs != null) {
            Attribute dn = attrs.get(this.distinguishedNameAttribute);
            if (dn != null) {
               userDN = (String)dn.get();
            }
         }

         if (userDN == null) {
            if (!sr.isRelative()) {
               throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);
            }

            userDN = name + ("".equals(baseDN) ? "" : "," + baseDN);
         }

         this.safeClose(results);
         results = null;
         InitialLdapContext userCtx = this.constructInitialLdapContext(userDN, credential);
         this.safeClose(userCtx);
         return userDN;
      }
   }

   private void traceLDAPEnv(Properties env) {
      Properties tmp = new Properties();
      tmp.putAll(env);
      if (tmp.containsKey("java.naming.security.credentials")) {
         tmp.setProperty("java.naming.security.credentials", "******");
      }

      if (tmp.containsKey("bindCredential")) {
         tmp.setProperty("bindCredential", "******");
      }

      PicketBoxLogger.LOGGER.traceLDAPConnectionEnv(tmp);
   }

   protected void safeClose(NamingEnumeration results) {
      if (results != null) {
         try {
            results.close();
         } catch (NamingException var3) {
         }
      }

   }

   protected void safeClose(InitialLdapContext ic) {
      if (ic != null) {
         try {
            ic.close();
         } catch (NamingException var3) {
         }
      }

   }
}
