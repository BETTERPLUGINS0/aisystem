package fr.xephi.authme.libs.org.jboss.security.auth.spi;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.SimpleGroup;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultUtil;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Arrays;
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
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

public class LdapLoginModule extends UsernamePasswordLoginModule {
   private static final String PRINCIPAL_DN_PREFIX_OPT = "principalDNPrefix";
   private static final String PRINCIPAL_DN_SUFFIX_OPT = "principalDNSuffix";
   private static final String ROLES_CTX_DN_OPT = "rolesCtxDN";
   private static final String USER_ROLES_CTX_DN_ATTRIBUTE_ID_OPT = "userRolesCtxDNAttributeName";
   private static final String UID_ATTRIBUTE_ID_OPT = "uidAttributeID";
   private static final String ROLE_ATTRIBUTE_ID_OPT = "roleAttributeID";
   private static final String MATCH_ON_USER_DN_OPT = "matchOnUserDN";
   private static final String ROLE_ATTRIBUTE_IS_DN_OPT = "roleAttributeIsDN";
   private static final String ROLE_NAME_ATTRIBUTE_ID_OPT = "roleNameAttributeID";
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
   private static final String SEARCH_SCOPE_OPT = "searchScope";
   private static final String SECURITY_DOMAIN_OPT = "jaasSecurityDomain";
   private static final String ALLOW_EMPTY_PASSWORDS = "allowEmptyPasswords";
   private static final String BIND_CREDENTIAL = "bindCredential";
   private static final String[] ALL_VALID_OPTIONS = new String[]{"principalDNPrefix", "principalDNSuffix", "rolesCtxDN", "userRolesCtxDNAttributeName", "uidAttributeID", "roleAttributeID", "matchOnUserDN", "roleAttributeIsDN", "roleNameAttributeID", "searchTimeLimit", "searchScope", "jaasSecurityDomain", "allowEmptyPasswords", "java.naming.factory.initial", "java.naming.factory.object", "java.naming.factory.state", "java.naming.factory.url.pkgs", "java.naming.provider.url", "java.naming.dns.url", "java.naming.authoritative", "java.naming.batchsize", "java.naming.referral", "java.naming.security.protocol", "java.naming.security.authentication", "java.naming.security.principal", "java.naming.security.credentials", "java.naming.language", "java.naming.applet"};
   private transient SimpleGroup userRoles = new SimpleGroup("Roles");

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
      this.addValidOptions(ALL_VALID_OPTIONS);
      super.initialize(subject, callbackHandler, sharedState, options);
   }

   protected String getUsersPassword() throws LoginException {
      return "";
   }

   protected Group[] getRoleSets() throws LoginException {
      Group[] roleSets = new Group[]{this.userRoles};
      return roleSets;
   }

   protected boolean validatePassword(String inputPassword, String expectedPassword) {
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
            isValid = true;
         } catch (Throwable var6) {
            super.setValidateError(var6);
         }
      }

      return isValid;
   }

   private void createLdapInitContext(String username, Object credential) throws Exception {
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

      String bindDN = (String)this.options.get("java.naming.security.principal");
      String bindCredential = (String)this.options.get("java.naming.security.credentials");
      String securityDomain = (String)this.options.get("jaasSecurityDomain");
      if (securityDomain != null) {
         ObjectName serviceName = new ObjectName(securityDomain);
         char[] tmp = DecodeAction.decode(bindCredential, serviceName);
         bindCredential = new String(tmp);
      }

      if (bindCredential != null && SecurityVaultUtil.isVaultFormat(bindCredential)) {
         bindCredential = SecurityVaultUtil.getValueAsString(bindCredential);
      }

      String principalDNPrefix = (String)this.options.get("principalDNPrefix");
      if (principalDNPrefix == null) {
         principalDNPrefix = "";
      }

      String principalDNSuffix = (String)this.options.get("principalDNSuffix");
      if (principalDNSuffix == null) {
         principalDNSuffix = "";
      }

      String matchType = (String)this.options.get("matchOnUserDN");
      boolean matchOnUserDN = Boolean.valueOf(matchType);
      String userDN = principalDNPrefix + username + principalDNSuffix;
      env.setProperty("java.naming.provider.url", providerURL);
      env.setProperty("java.naming.security.principal", userDN);
      env.put("java.naming.security.credentials", credential);
      this.traceLDAPEnv(env);
      InitialLdapContext ctx = null;
      ClassLoader currentTCCL = SecurityActions.getContextClassLoader();

      try {
         if (currentTCCL != null) {
            SecurityActions.setContextClassLoader((ClassLoader)null);
         }

         ctx = new InitialLdapContext(env, (Control[])null);
         PicketBoxLogger.LOGGER.traceSuccessfulLogInToLDAP(ctx.toString());
         if (bindDN != null) {
            PicketBoxLogger.LOGGER.traceRebindWithConfiguredPrincipal(bindDN);
            env.setProperty("java.naming.security.principal", bindDN);
            env.put("java.naming.security.credentials", bindCredential);
            ctx = new InitialLdapContext(env, (Control[])null);
         }

         String rolesCtxDN = (String)this.options.get("rolesCtxDN");
         String userRolesCtxDNAttributeName = (String)this.options.get("userRolesCtxDNAttributeName");
         if (userRolesCtxDNAttributeName != null) {
            String[] returnAttribute = new String[]{userRolesCtxDNAttributeName};

            try {
               Attributes result = ctx.getAttributes(userDN, returnAttribute);
               if (result.get(userRolesCtxDNAttributeName) != null) {
                  rolesCtxDN = result.get(userRolesCtxDNAttributeName).get().toString();
                  PicketBoxLogger.LOGGER.traceFoundUserRolesContextDN(rolesCtxDN);
               }
            } catch (NamingException var62) {
               PicketBoxLogger.LOGGER.debugFailureToQueryLDAPAttribute(userRolesCtxDNAttributeName, userDN, var62);
            }
         }

         if (rolesCtxDN != null) {
            String uidAttrName = (String)this.options.get("uidAttributeID");
            if (uidAttrName == null) {
               uidAttrName = "uid";
            }

            String roleAttrName = (String)this.options.get("roleAttributeID");
            if (roleAttrName == null) {
               roleAttrName = "roles";
            }

            StringBuffer roleFilter = new StringBuffer("(");
            roleFilter.append(uidAttrName);
            roleFilter.append("={0})");
            String userToMatch = username;
            if (matchOnUserDN) {
               userToMatch = userDN;
            }

            String[] roleAttr = new String[]{roleAttrName};
            String roleAttributeIsDNOption = (String)this.options.get("roleAttributeIsDN");
            boolean roleAttributeIsDN = Boolean.valueOf(roleAttributeIsDNOption);
            String roleNameAttributeID = (String)this.options.get("roleNameAttributeID");
            if (roleNameAttributeID == null) {
               roleNameAttributeID = "name";
            }

            int searchScope = 2;
            int searchTimeLimit = 10000;
            String timeLimit = (String)this.options.get("searchTimeLimit");
            if (timeLimit != null) {
               try {
                  searchTimeLimit = Integer.parseInt(timeLimit);
               } catch (NumberFormatException var61) {
                  PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty("searchTimeLimit", (long)searchTimeLimit);
               }
            }

            String scope = (String)this.options.get("searchScope");
            if ("OBJECT_SCOPE".equalsIgnoreCase(scope)) {
               searchScope = 0;
            } else if ("ONELEVEL_SCOPE".equalsIgnoreCase(scope)) {
               searchScope = 1;
            }

            if ("SUBTREE_SCOPE".equalsIgnoreCase(scope)) {
               searchScope = 2;
            }

            NamingEnumeration answer = null;

            try {
               SearchControls controls = new SearchControls();
               controls.setSearchScope(searchScope);
               controls.setReturningAttributes(roleAttr);
               controls.setTimeLimit(searchTimeLimit);
               Object[] filterArgs = new Object[]{userToMatch};
               PicketBoxLogger.LOGGER.traceRolesDNSearch(rolesCtxDN, roleFilter.toString(), userToMatch, Arrays.toString(roleAttr), searchScope, searchTimeLimit);
               answer = ctx.search(rolesCtxDN, roleFilter.toString(), filterArgs, controls);

               while(true) {
                  while(answer.hasMore()) {
                     SearchResult sr = (SearchResult)answer.next();
                     PicketBoxLogger.LOGGER.traceCheckSearchResult(sr.getName());
                     Attributes attrs = sr.getAttributes();
                     Attribute roles = attrs.get(roleAttrName);
                     if (roles != null) {
                        for(int r = 0; r < roles.size(); ++r) {
                           Object value = roles.get(r);
                           String roleName = null;
                           if (roleAttributeIsDN) {
                              String roleDN = value.toString();
                              String[] returnAttribute = new String[]{roleNameAttributeID};
                              PicketBoxLogger.LOGGER.traceFollowRoleDN(roleDN);

                              try {
                                 Attributes result2 = ctx.getAttributes(roleDN, returnAttribute);
                                 Attribute roles2 = result2.get(roleNameAttributeID);
                                 if (roles2 != null) {
                                    for(int m = 0; m < roles2.size(); ++m) {
                                       roleName = (String)roles2.get(m);
                                       this.addRole(roleName);
                                    }
                                 }
                              } catch (NamingException var63) {
                                 PicketBoxLogger.LOGGER.debugFailureToQueryLDAPAttribute(roleNameAttributeID, roleDN, var63);
                              }
                           } else {
                              roleName = value.toString();
                              this.addRole(roleName);
                           }
                        }
                     } else {
                        PicketBoxLogger.LOGGER.debugFailureToFindAttrInSearchResult(roleAttrName, sr.getName());
                     }
                  }

                  return;
               }
            } catch (NamingException var64) {
               PicketBoxLogger.LOGGER.debugFailureToExecuteRolesDNSearch(var64);
            } finally {
               if (answer != null) {
                  answer.close();
               }

            }
         }
      } finally {
         if (ctx != null) {
            ctx.close();
         }

         if (currentTCCL != null) {
            SecurityActions.setContextClassLoader(currentTCCL);
         }

      }

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
