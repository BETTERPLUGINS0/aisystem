package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import javax.management.ObjectName;
import javax.naming.CompositeName;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.ReferralException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class LdapExtLoginModule extends UsernamePasswordLoginModule {
   private static final String ROLES_CTX_DN_OPT = "rolesCtxDN";
   private static final String ROLE_ATTRIBUTE_ID_OPT = "roleAttributeID";
   private static final String ROLE_ATTRIBUTE_IS_DN_OPT = "roleAttributeIsDN";
   private static final String ROLE_NAME_ATTRIBUTE_ID_OPT = "roleNameAttributeID";
   private static final String PARSE_ROLE_NAME_FROM_DN_OPT = "parseRoleNameFromDN";
   private static final String BIND_DN = "bindDN";
   private static final String BIND_CREDENTIAL = "bindCredential";
   private static final String BASE_CTX_DN = "baseCtxDN";
   private static final String BASE_FILTER_OPT = "baseFilter";
   private static final String ROLE_FILTER_OPT = "roleFilter";
   private static final String ROLE_RECURSION = "roleRecursion";
   private static final String DEFAULT_ROLE = "defaultRole";
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
   private static final String SEARCH_SCOPE_OPT = "searchScope";
   private static final String SECURITY_DOMAIN_OPT = "jaasSecurityDomain";
   private static final String DISTINGUISHED_NAME_ATTRIBUTE_OPT = "distinguishedNameAttribute";
   private static final String PARSE_USERNAME = "parseUsername";
   private static final String USERNAME_BEGIN_STRING = "usernameBeginString";
   private static final String USERNAME_END_STRING = "usernameEndString";
   private static final String ALLOW_EMPTY_PASSWORDS = "allowEmptyPasswords";
   private static final String REFERRAL_USER_ATTRIBUTE_ID_TO_CHECK = "referralUserAttributeIDToCheck";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"rolesCtxDN", "roleAttributeID", "roleAttributeIsDN", "roleNameAttributeID", "parseRoleNameFromDN", "bindDN", "bindCredential", "baseCtxDN", "baseFilter", "roleFilter", "roleRecursion", "defaultRole", "searchTimeLimit", "searchScope", "jaasSecurityDomain", "distinguishedNameAttribute", "parseUsername", "usernameBeginString", "usernameEndString", "allowEmptyPasswords", "referralUserAttributeIDToCheck", "java.naming.factory.initial", "java.naming.factory.object", "java.naming.factory.state", "java.naming.factory.url.pkgs", "java.naming.provider.url", "java.naming.dns.url", "java.naming.authoritative", "java.naming.batchsize", "java.naming.referral", "java.naming.security.protocol", "java.naming.security.authentication", "java.naming.security.principal", "java.naming.security.credentials", "java.naming.language", "java.naming.applet"};
   protected String bindDN;
   protected String bindCredential;
   protected String baseDN;
   protected String baseFilter;
   protected String rolesCtxDN;
   protected String roleFilter;
   protected String roleAttributeID;
   protected String roleNameAttributeID;
   protected boolean roleAttributeIsDN;
   protected boolean parseRoleNameFromDN;
   protected int recursion = 0;
   protected int searchTimeLimit = 10000;
   protected int searchScope = 2;
   protected String distinguishedNameAttribute;
   protected boolean parseUsername;
   protected String usernameBeginString;
   protected String usernameEndString;
   protected boolean isPasswordValidated = false;
   protected String referralUserAttributeIDToCheck = null;
   private transient SimpleGroup userRoles = new SimpleGroup("Roles");

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
   }

   protected String getUsersPassword() throws LoginException {
      return "";
   }

   protected Group[] getRoleSets() throws LoginException {
      if (!this.isPasswordValidated && this.getIdentity() != this.unauthenticatedIdentity) {
         try {
            String username = this.getUsername();
            PicketBoxLogger.LOGGER.traceBindingLDAPUsername(username);
            this.createLdapInitContext(username, (Object)null);
            this.defaultRole();
         } catch (Exception var3) {
            LoginException le = new LoginException();
            le.initCause(var3);
            throw le;
         }
      }

      Group[] roleSets = new Group[]{this.userRoles};
      return roleSets;
   }

   protected boolean validatePassword(String inputPassword, String expectedPassword) {
      this.isPasswordValidated = true;
      boolean isValid = false;
      if (inputPassword != null) {
         if (inputPassword.length() == 0) {
            boolean allowEmptyPasswords = false;
            String flag = (String)this.options.get("allowEmptyPasswords");
            if (flag != null) {
               allowEmptyPasswords = Boolean.valueOf(flag);
            }

            if (!allowEmptyPasswords) {
               PicketBoxLogger.LOGGER.traceRejectingEmptyPassword();
               return false;
            }
         }

         try {
            String username = this.getUsername();
            this.createLdapInitContext(username, inputPassword);
            this.defaultRole();
            isValid = true;
         } catch (Throwable var6) {
            super.setValidateError(var6);
         }
      }

      return isValid;
   }

   private void defaultRole() {
      String defaultRole = (String)this.options.get("defaultRole");

      try {
         if (defaultRole == null || defaultRole.equals("")) {
            return;
         }

         Principal p = super.createIdentity(defaultRole);
         PicketBoxLogger.LOGGER.traceAssignUserToRole(defaultRole);
         this.userRoles.addMember(p);
      } catch (Exception var3) {
         PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(defaultRole, var3);
      }

   }

   private boolean createLdapInitContext(String username, Object credential) throws Exception {
      this.bindDN = (String)this.options.get("bindDN");
      this.bindCredential = (String)this.options.get("bindCredential");
      if (this.bindCredential != null && fr.xephi.authme.libs.org.jboss.security.Util.isPasswordCommand(this.bindCredential)) {
         this.bindCredential = new String(fr.xephi.authme.libs.org.jboss.security.Util.loadPassword(this.bindCredential));
      }

      String securityDomain = (String)this.options.get("jaasSecurityDomain");
      if (securityDomain != null) {
         ObjectName serviceName = new ObjectName(securityDomain);
         char[] tmp = DecodeAction.decode(this.bindCredential, serviceName);
         this.bindCredential = new String(tmp);
      }

      if (this.bindCredential != null && SecurityVaultUtil.isVaultFormat(this.bindCredential)) {
         this.bindCredential = SecurityVaultUtil.getValueAsString(this.bindCredential);
      }

      this.baseDN = (String)this.options.get("baseCtxDN");
      this.baseFilter = (String)this.options.get("baseFilter");
      this.roleFilter = (String)this.options.get("roleFilter");
      this.roleAttributeID = (String)this.options.get("roleAttributeID");
      if (this.roleAttributeID == null) {
         this.roleAttributeID = "role";
      }

      String roleAttributeIsDNOption = (String)this.options.get("roleAttributeIsDN");
      this.roleAttributeIsDN = Boolean.valueOf(roleAttributeIsDNOption);
      this.roleNameAttributeID = (String)this.options.get("roleNameAttributeID");
      if (this.roleNameAttributeID == null) {
         this.roleNameAttributeID = "name";
      }

      this.referralUserAttributeIDToCheck = (String)this.options.get("referralUserAttributeIDToCheck");
      String parseRoleNameFromDNOption = (String)this.options.get("parseRoleNameFromDN");
      this.parseRoleNameFromDN = Boolean.valueOf(parseRoleNameFromDNOption);
      this.rolesCtxDN = (String)this.options.get("rolesCtxDN");
      String strRecursion = (String)this.options.get("roleRecursion");

      try {
         this.recursion = Integer.parseInt(strRecursion);
      } catch (NumberFormatException var20) {
         PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty("roleRecursion", 0L);
         this.recursion = 0;
      }

      String timeLimit = (String)this.options.get("searchTimeLimit");
      if (timeLimit != null) {
         try {
            this.searchTimeLimit = Integer.parseInt(timeLimit);
         } catch (NumberFormatException var19) {
            PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty("searchTimeLimit", (long)this.searchTimeLimit);
         }
      }

      String scope = (String)this.options.get("searchScope");
      if ("OBJECT_SCOPE".equalsIgnoreCase(scope)) {
         this.searchScope = 0;
      } else if ("ONELEVEL_SCOPE".equalsIgnoreCase(scope)) {
         this.searchScope = 1;
      }

      if ("SUBTREE_SCOPE".equalsIgnoreCase(scope)) {
         this.searchScope = 2;
      }

      this.distinguishedNameAttribute = (String)this.options.get("distinguishedNameAttribute");
      if (this.distinguishedNameAttribute == null) {
         this.distinguishedNameAttribute = "distinguishedName";
      }

      InitialLdapContext ctx = null;
      ClassLoader currentTCCL = SecurityActions.getContextClassLoader();

      try {
         if (currentTCCL != null) {
            SecurityActions.setContextClassLoader((ClassLoader)null);
         }

         ctx = this.constructInitialLdapContext(this.bindDN, this.bindCredential);
         String userDN = this.bindDNAuthentication(ctx, username, credential, this.baseDN, this.baseFilter);
         SearchControls constraints = new SearchControls();
         constraints.setSearchScope(this.searchScope);
         constraints.setTimeLimit(this.searchTimeLimit);
         String[] attrList;
         if (this.referralUserAttributeIDToCheck != null) {
            attrList = new String[]{this.roleAttributeID, this.referralUserAttributeIDToCheck};
         } else {
            attrList = new String[]{this.roleAttributeID};
         }

         constraints.setReturningAttributes(attrList);
         this.rolesSearch(ctx, constraints, username, userDN, this.recursion, 0);
      } catch (Exception var21) {
         throw var21;
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

   protected String bindDNAuthentication(InitialLdapContext ctx, String user, Object credential, String baseDN, String filter) throws NamingException {
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(2);
      constraints.setTimeLimit(this.searchTimeLimit);
      String[] attrList = new String[]{this.distinguishedNameAttribute};
      constraints.setReturningAttributes(attrList);
      NamingEnumeration results = null;
      Object[] filterArgs = new Object[]{user};
      LdapContext ldapCtx = ctx;
      boolean referralsLeft = true;
      SearchResult sr = null;

      while(referralsLeft) {
         try {
            results = ((LdapContext)ldapCtx).search(baseDN, filter, filterArgs, constraints);
            if (results.hasMore()) {
               sr = (SearchResult)results.next();
            }

            referralsLeft = false;
         } catch (ReferralException var17) {
            ldapCtx = (LdapContext)var17.getReferralContext();
            if (results != null) {
               results.close();
            }
         }
      }

      if (sr == null) {
         results.close();
         throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
      } else {
         String name = sr.getName();
         String userDN = null;
         Attributes attrs = sr.getAttributes();
         if (attrs != null) {
            Attribute dn = attrs.get(this.distinguishedNameAttribute);
            if (dn != null) {
               userDN = (String)dn.get();
            }
         }

         results.close();
         results = null;
         InitialLdapContext userCtx;
         if (userDN == null) {
            if (sr.isRelative()) {
               userDN = (new CompositeName(name)).get(0) + ("".equals(baseDN) ? "" : "," + baseDN);
               if (this.isPasswordValidated) {
                  userCtx = this.constructInitialLdapContext(userDN, credential);
                  userCtx.close();
               }
            } else {
               userDN = this.bindDNReferralAuthentication(sr.getName(), credential);
               if (userDN == null) {
                  throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);
               }
            }
         } else if (this.isPasswordValidated) {
            userCtx = this.constructInitialLdapContext(userDN, credential);
            userCtx.close();
         }

         return userDN;
      }
   }

   private String bindDNReferralAuthentication(String absoluteName, Object credential) throws NamingException {
      URI uri;
      try {
         uri = new URI(absoluteName);
      } catch (URISyntaxException var8) {
         throw PicketBoxMessages.MESSAGES.unableToParseReferralAbsoluteName(var8, absoluteName);
      }

      String name = uri.getPath().substring(1);
      String namingProviderURL = uri.getScheme() + "://" + uri.getAuthority();
      Properties refEnv = this.constructLdapContextEnvironment(namingProviderURL, name, credential);
      InitialLdapContext refCtx = new InitialLdapContext(refEnv, (Control[])null);
      refCtx.close();
      return name;
   }

   protected void rolesSearch(LdapContext ctx, SearchControls constraints, String user, String userDN, int recursionMax, int nesting) throws NamingException {
      LdapContext ldapCtx = ctx;
      Object[] filterArgs = new Object[]{user, this.sanitizeDN(userDN)};
      boolean referralsExist = true;

      while(referralsExist) {
         NamingEnumeration results = ldapCtx.search(this.rolesCtxDN, this.roleFilter, filterArgs, constraints);

         try {
            while(results.hasMore()) {
               SearchResult sr = (SearchResult)results.next();
               String dn;
               if (sr.isRelative()) {
                  dn = this.canonicalize(sr.getName());
               } else {
                  dn = sr.getNameInNamespace();
               }

               String[] attrNames;
               Attributes result;
               Attribute roles2;
               int m;
               String roleName;
               if (nesting == 0 && this.roleAttributeIsDN && this.roleNameAttributeID != null) {
                  if (this.parseRoleNameFromDN) {
                     this.parseRole(dn);
                  } else {
                     attrNames = new String[]{this.roleNameAttributeID};
                     result = null;
                     if (sr.isRelative()) {
                        result = ldapCtx.getAttributes(this.quoteDN(dn), attrNames);
                     } else {
                        result = this.getAttributesFromReferralEntity(sr, user, userDN);
                     }

                     roles2 = result != null ? result.get(this.roleNameAttributeID) : null;
                     if (roles2 != null) {
                        for(m = 0; m < roles2.size(); ++m) {
                           roleName = (String)roles2.get(m);
                           this.addRole(roleName);
                        }
                     }
                  }
               }

               attrNames = new String[]{this.roleAttributeID};
               result = null;
               if (sr.isRelative()) {
                  result = ldapCtx.getAttributes(this.quoteDN(dn), attrNames);
               } else {
                  result = this.getAttributesFromReferralEntity(sr, user, userDN);
               }

               if (result != null && result.size() > 0) {
                  roles2 = result.get(this.roleAttributeID);

                  for(m = 0; m < roles2.size(); ++m) {
                     roleName = (String)roles2.get(m);
                     if (this.roleAttributeIsDN && this.parseRoleNameFromDN) {
                        this.parseRole(roleName);
                     } else if (this.roleAttributeIsDN) {
                        String roleDN = this.quoteDN(roleName);
                        String[] returnAttribute = new String[]{this.roleNameAttributeID};

                        try {
                           Attributes result2 = null;
                           if (sr.isRelative()) {
                              result2 = ldapCtx.getAttributes(roleDN, returnAttribute);
                           } else {
                              result2 = this.getAttributesFromReferralEntity(sr, user, userDN);
                           }

                           Attribute roles2 = result2 != null ? result2.get(this.roleNameAttributeID) : null;
                           if (roles2 != null) {
                              for(int m = 0; m < roles2.size(); ++m) {
                                 roleName = (String)roles2.get(m);
                                 this.addRole(roleName);
                              }
                           }
                        } catch (NamingException var27) {
                           PicketBoxLogger.LOGGER.debugFailureToQueryLDAPAttribute(this.roleNameAttributeID, roleDN, var27);
                        }
                     } else {
                        this.addRole(roleName);
                     }
                  }
               }

               if (nesting < recursionMax) {
                  this.rolesSearch(ldapCtx, constraints, user, dn, recursionMax, nesting + 1);
               }
            }

            referralsExist = false;
         } catch (ReferralException var28) {
            ldapCtx = (LdapContext)var28.getReferralContext();
         } finally {
            if (results != null) {
               results.close();
            }

         }
      }

   }

   private String sanitizeDN(String dn) {
      return dn != null && dn.startsWith("\"") && dn.endsWith("\"") ? dn.substring(1, dn.length() - 1) : dn;
   }

   private String quoteDN(String dn) {
      return dn != null && !dn.startsWith("\"") && !dn.endsWith("\"") && dn.indexOf("/") > -1 ? "\"" + dn + "\"" : dn;
   }

   private Attributes getAttributesFromReferralEntity(SearchResult sr, String... users) throws NamingException {
      Attributes result = sr.getAttributes();
      boolean chkSuccessful = false;
      if (this.referralUserAttributeIDToCheck != null) {
         Attribute usersToCheck = result.get(this.referralUserAttributeIDToCheck);

         for(int i = 0; usersToCheck != null && i < usersToCheck.size(); ++i) {
            String userDNToCheck = (String)usersToCheck.get(i);
            String[] arr$ = users;
            int len$ = users.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String u = arr$[i$];
               if (u.equals(userDNToCheck)) {
                  chkSuccessful = true;
                  return chkSuccessful ? result : null;
               }
            }
         }
      }

      return chkSuccessful ? result : null;
   }

   private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException {
      String protocol = (String)this.options.get("java.naming.security.protocol");
      String providerURL = (String)this.options.get("java.naming.provider.url");
      if (providerURL == null) {
         providerURL = "ldap://localhost:" + (protocol != null && protocol.equals("ssl") ? "636" : "389");
      }

      Properties env = this.constructLdapContextEnvironment(providerURL, dn, credential);
      return new InitialLdapContext(env, (Control[])null);
   }

   private Properties constructLdapContextEnvironment(String namingProviderURL, String principalDN, Object credential) {
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

      if (namingProviderURL != null) {
         env.setProperty("java.naming.provider.url", namingProviderURL);
      }

      if (principalDN != null) {
         env.setProperty("java.naming.security.principal", principalDN);
      }

      if (credential != null) {
         env.put("java.naming.security.credentials", credential);
      }

      this.traceLDAPEnv(env);
      return env;
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

   private String canonicalize(String searchResult) {
      int len = searchResult.length();
      String appendRolesCtxDN = "" + ("".equals(this.rolesCtxDN) ? "" : "," + this.rolesCtxDN);
      String result;
      if (searchResult.endsWith("\"")) {
         result = searchResult.substring(0, len - 1) + appendRolesCtxDN + "\"";
      } else {
         result = searchResult + appendRolesCtxDN;
      }

      return result;
   }

   private void addRole(String roleName) {
      if (roleName != null) {
         try {
            Principal p = super.createIdentity(roleName);
            PicketBoxLogger.LOGGER.traceAssignUserToRole(roleName);
            this.userRoles.addMember(p);
         } catch (Exception var3) {
            PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(roleName, var3);
         }
      }

   }

   private void parseRole(String dn) {
      this.parseRole(dn, this.roleNameAttributeID);
   }

   private void parseRole(String dn, String roleNameAttributeIdentifier) {
      StringTokenizer st = new StringTokenizer(dn, ",");

      while(st != null && st.hasMoreTokens()) {
         String keyVal = st.nextToken();
         if (keyVal.indexOf(roleNameAttributeIdentifier) > -1) {
            StringTokenizer kst = new StringTokenizer(keyVal, "=");
            kst.nextToken();
            this.addRole(kst.nextToken());
         }
      }

   }

   protected String getUsername() {
      String username = super.getUsername();
      this.parseUsername = Boolean.valueOf((String)this.options.get("parseUsername"));
      if (this.parseUsername) {
         this.usernameBeginString = (String)this.options.get("usernameBeginString");
         this.usernameEndString = (String)this.options.get("usernameEndString");
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
}
