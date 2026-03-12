package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class LdapUsersLoginModule extends UsernamePasswordLoginModule {
   private static final String BIND_DN = "bindDN";
   private static final String BIND_CREDENTIAL = "bindCredential";
   private static final String BASE_CTX_DN = "baseCtxDN";
   private static final String BASE_FILTER_OPT = "baseFilter";
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
   private static final String SEARCH_SCOPE_OPT = "searchScope";
   private static final String DISTINGUISHED_NAME_ATTRIBUTE_OPT = "distinguishedNameAttribute";
   private static final String PARSE_USERNAME = "parseUsername";
   private static final String USERNAME_BEGIN_STRING = "usernameBeginString";
   private static final String USERNAME_END_STRING = "usernameEndString";
   private static final String ALLOW_EMPTY_PASSWORDS = "allowEmptyPasswords";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"bindDN", "bindCredential", "baseCtxDN", "baseFilter", "searchTimeLimit", "searchScope", "distinguishedNameAttribute", "parseUsername", "usernameBeginString", "usernameEndString", "allowEmptyPasswords", "java.naming.factory.initial", "java.naming.security.authentication", "java.naming.security.protocol", "java.naming.provider.url", "java.naming.security.principal", "java.naming.security.credentials"};
   protected String bindDN;
   protected String bindCredential;
   protected String baseDN;
   protected String baseFilter;
   protected int searchTimeLimit = 10000;
   protected int searchScope = 2;
   protected String distinguishedNameAttribute;
   protected boolean parseUsername;
   protected String usernameBeginString;
   protected String usernameEndString;
   protected boolean allowEmptyPasswords;

   protected String getUsersPassword() throws LoginException {
      return "";
   }

   protected Group[] getRoleSets() throws LoginException {
      return new Group[0];
   }

   protected String getUsername() {
      String username = super.getUsername();
      if (this.parseUsername) {
         int beginIndex = 0;
         if (this.usernameBeginString != null && !this.usernameBeginString.equals("")) {
            beginIndex = username.indexOf(this.usernameBeginString) + this.usernameBeginString.length();
         }

         if (beginIndex == -1) {
            beginIndex = 0;
         }

         int endIndex = username.length();
         if (this.usernameEndString != null && !this.usernameEndString.equals("")) {
            endIndex = username.substring(beginIndex).indexOf(this.usernameEndString);
         }

         if (endIndex == -1) {
            endIndex = username.length();
         } else {
            endIndex += beginIndex;
         }

         username = username.substring(beginIndex, endIndex);
      }

      return username;
   }

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
      this.bindDN = (String)options.get("bindDN");
      this.bindCredential = (String)options.get("bindCredential");
      if (this.bindCredential != null && fr.xephi.authme.libs.org.jboss.security.Util.isPasswordCommand(this.bindCredential)) {
         try {
            this.bindCredential = new String(fr.xephi.authme.libs.org.jboss.security.Util.loadPassword(this.bindCredential));
         } catch (Exception var8) {
            throw PicketBoxMessages.MESSAGES.failedToDecodeBindCredential(var8);
         }
      }

      this.baseDN = (String)options.get("baseCtxDN");
      this.baseFilter = (String)options.get("baseFilter");
      String timeLimit = (String)options.get("searchTimeLimit");
      if (timeLimit != null) {
         try {
            this.searchTimeLimit = Integer.parseInt(timeLimit);
         } catch (NumberFormatException var7) {
            PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty("searchTimeLimit", (long)this.searchTimeLimit);
         }
      }

      String scope = (String)options.get("searchScope");
      if ("OBJECT_SCOPE".equalsIgnoreCase(scope)) {
         this.searchScope = 0;
      } else if ("ONELEVEL_SCOPE".equalsIgnoreCase(scope)) {
         this.searchScope = 1;
      }

      if ("SUBTREE_SCOPE".equalsIgnoreCase(scope)) {
         this.searchScope = 2;
      }

      this.distinguishedNameAttribute = (String)options.get("distinguishedNameAttribute");
      if (this.distinguishedNameAttribute == null) {
         this.distinguishedNameAttribute = "distinguishedName";
      }

      this.allowEmptyPasswords = Boolean.valueOf((String)options.get("allowEmptyPasswords"));
      this.parseUsername = Boolean.valueOf((String)options.get("parseUsername"));
      if (this.parseUsername) {
         this.usernameBeginString = (String)options.get("usernameBeginString");
         this.usernameEndString = (String)options.get("usernameEndString");
      }

   }

   protected boolean validatePassword(String inputPassword, String expectedPassword) {
      boolean isValid = false;
      if (inputPassword != null) {
         if (inputPassword.length() == 0 && !this.allowEmptyPasswords) {
            PicketBoxLogger.LOGGER.traceRejectingEmptyPassword();
            return false;
         }

         try {
            String username = this.getUsername();
            isValid = this.createLdapInitContext(username, inputPassword);
         } catch (Throwable var5) {
            super.setValidateError(var5);
         }
      }

      return isValid;
   }

   private boolean createLdapInitContext(String username, Object credential) throws Exception {
      InitialLdapContext ctx = null;
      ClassLoader currentTCCL = SecurityActions.getContextClassLoader();

      try {
         if (currentTCCL != null) {
            SecurityActions.setContextClassLoader((ClassLoader)null);
         }

         ctx = this.constructInitialLdapContext(this.bindDN, this.bindCredential);
         this.bindDNAuthentication(ctx, username, credential, this.baseDN, this.baseFilter);
      } catch (Exception var9) {
         throw var9;
      } finally {
         if (ctx != null) {
            ctx.close();
         }

         if (currentTCCL != null) {
            SecurityActions.setContextClassLoader(currentTCCL);
         }

      }

      return true;
   }

   private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException {
      Properties env = new Properties();
      Iterator iter = this.options.entrySet().iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
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
      constraints.setSearchScope(this.searchScope);
      constraints.setTimeLimit(this.searchTimeLimit);
      String[] attrList = new String[]{this.distinguishedNameAttribute};
      constraints.setReturningAttributes(attrList);
      NamingEnumeration<SearchResult> results = null;
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

         results.close();
         results = null;
         InitialLdapContext userCtx = this.constructInitialLdapContext(userDN, credential);
         userCtx.close();
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
}
