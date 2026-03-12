package fr.xephi.authme.libs.org.jboss.security.mapping.providers.role;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.identity.plugins.SimpleRole;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultException;
import fr.xephi.authme.libs.org.jboss.security.vault.SecurityVaultUtil;
import java.security.Principal;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;

public class LdapRolesMappingProvider extends AbstractRolesMappingProvider {
   private static final String BIND_DN = "bindDN";
   private static final String BIND_CREDENTIAL = "bindCredential";
   private static final String ROLES_CTX_DN_OPT = "rolesCtxDN";
   private static final String ROLE_ATTRIBUTE_ID_OPT = "roleAttributeID";
   private static final String ROLE_ATTRIBUTE_IS_DN_OPT = "roleAttributeIsDN";
   private static final String ROLE_NAME_ATTRIBUTE_ID_OPT = "roleNameAttributeID";
   private static final String PARSE_ROLE_NAME_FROM_DN_OPT = "parseRoleNameFromDN";
   private static final String ROLE_FILTER_OPT = "roleFilter";
   private static final String ROLE_RECURSION = "roleRecursion";
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
   private static final String SEARCH_SCOPE_OPT = "searchScope";
   protected String bindDN;
   protected String bindCredential;
   protected String rolesCtxDN;
   protected String roleFilter;
   protected String roleAttributeID;
   protected String roleNameAttributeID;
   protected boolean roleAttributeIsDN;
   protected boolean parseRoleNameFromDN;
   protected int recursion = 0;
   protected int searchTimeLimit = 10000;
   protected int searchScope = 2;
   protected Map<String, Object> options;

   public void init(Map<String, Object> options) {
      if (options != null) {
         this.options = options;
         this.bindDN = (String)options.get("bindDN");
         this.bindCredential = (String)options.get("bindCredential");
         if (this.bindCredential != null && fr.xephi.authme.libs.org.jboss.security.Util.isPasswordCommand(this.bindCredential)) {
            try {
               this.bindCredential = new String(fr.xephi.authme.libs.org.jboss.security.Util.loadPassword(this.bindCredential));
            } catch (Exception var10) {
               throw PicketBoxMessages.MESSAGES.failedToDecodeBindCredential(var10);
            }
         }

         if (this.bindCredential != null && SecurityVaultUtil.isVaultFormat(this.bindCredential)) {
            try {
               this.bindCredential = SecurityVaultUtil.getValueAsString(this.bindCredential);
            } catch (SecurityVaultException var9) {
               throw new IllegalArgumentException(PicketBoxMessages.MESSAGES.unableToGetPasswordFromVault());
            }
         }

         this.roleFilter = (String)options.get("roleFilter");
         this.roleAttributeID = (String)options.get("roleAttributeID");
         if (this.roleAttributeID == null) {
            this.roleAttributeID = "role";
         }

         String roleAttributeIsDNOption = (String)options.get("roleAttributeIsDN");
         this.roleAttributeIsDN = Boolean.valueOf(roleAttributeIsDNOption);
         this.roleNameAttributeID = (String)options.get("roleNameAttributeID");
         if (this.roleNameAttributeID == null) {
            this.roleNameAttributeID = "name";
         }

         String parseRoleNameFromDNOption = (String)options.get("parseRoleNameFromDN");
         this.parseRoleNameFromDN = Boolean.valueOf(parseRoleNameFromDNOption);
         this.rolesCtxDN = (String)options.get("rolesCtxDN");
         String strRecursion = (String)options.get("roleRecursion");

         try {
            this.recursion = Integer.parseInt(strRecursion);
         } catch (Exception var8) {
            PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty("roleRecursion", 0L);
            this.recursion = 0;
         }

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
      }

   }

   public void performMapping(Map<String, Object> contextMap, RoleGroup mappedObject) {
      if (contextMap != null && !contextMap.isEmpty()) {
         Principal principal = this.getCallerPrincipal(contextMap);
         if (principal != null) {
            InitialLdapContext ctx = null;
            ClassLoader currentTCCL = SecurityActions.getContextClassLoader();

            try {
               if (currentTCCL != null) {
                  SecurityActions.setContextClassLoader((ClassLoader)null);
               }

               ctx = this.constructInitialLdapContext(this.bindDN, this.bindCredential);
               SearchControls constraints = new SearchControls();
               constraints.setSearchScope(this.searchScope);
               constraints.setReturningAttributes(new String[0]);
               constraints.setTimeLimit(this.searchTimeLimit);
               this.rolesSearch(ctx, constraints, principal.getName(), this.recursion, 0, mappedObject);
            } catch (NamingException var15) {
               PicketBoxLogger.LOGGER.debugIgnoredException(var15);
            } finally {
               if (ctx != null) {
                  try {
                     ctx.close();
                  } catch (NamingException var14) {
                     PicketBoxLogger.LOGGER.debugIgnoredException(var14);
                  }
               }

               if (currentTCCL != null) {
                  SecurityActions.setContextClassLoader(currentTCCL);
               }

            }
         }

      } else {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("contextMap");
      }
   }

   protected InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException {
      Properties env = new Properties();
      Iterator iter = this.options.entrySet().iterator();

      while(iter.hasNext()) {
         Entry<String, Object> entry = (Entry)iter.next();
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

   protected void rolesSearch(InitialLdapContext ctx, SearchControls constraints, String user, int recursionMax, int nesting, RoleGroup roleGroup) throws NamingException {
      Object[] filterArgs = new Object[]{user};
      NamingEnumeration results = ctx.search(this.rolesCtxDN, this.roleFilter, filterArgs, constraints);

      try {
         while(results.hasMore()) {
            SearchResult sr = (SearchResult)results.next();
            String dn = this.canonicalize(sr.getName());
            String[] attrNames = new String[]{this.roleAttributeID};
            Attributes result = ctx.getAttributes(dn, attrNames);
            if (result != null && result.size() > 0) {
               Attribute roles = result.get(this.roleAttributeID);

               for(int n = 0; n < roles.size(); ++n) {
                  String roleName = (String)roles.get(n);
                  if (this.roleAttributeIsDN && this.parseRoleNameFromDN) {
                     this.parseRole(roleName, roleGroup);
                  } else if (this.roleAttributeIsDN) {
                     String roleDN = roleName;
                     String[] returnAttribute = new String[]{this.roleNameAttributeID};
                     PicketBoxLogger.LOGGER.traceFollowRoleDN(roleName);

                     try {
                        Attributes result2 = ctx.getAttributes(roleDN, returnAttribute);
                        Attribute roles2 = result2.get(this.roleNameAttributeID);
                        if (roles2 != null) {
                           for(int m = 0; m < roles2.size(); ++m) {
                              roleName = (String)roles2.get(m);
                              this.addRole(roleName, roleGroup);
                           }
                        }
                     } catch (NamingException var24) {
                        PicketBoxLogger.LOGGER.debugFailureToQueryLDAPAttribute(this.roleNameAttributeID, roleName, var24);
                     }
                  } else {
                     this.addRole(roleName, roleGroup);
                  }
               }
            }

            if (nesting < recursionMax) {
               this.rolesSearch(ctx, constraints, user, recursionMax, nesting + 1, roleGroup);
            }
         }
      } finally {
         if (results != null) {
            results.close();
         }

      }

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

   private void addRole(String roleName, RoleGroup roleGroup) {
      if (roleName != null) {
         try {
            SimpleRole role = new SimpleRole(roleName);
            PicketBoxLogger.LOGGER.traceAssignUserToRole(roleName);
            roleGroup.addRole(role);
         } catch (Exception var4) {
            PicketBoxLogger.LOGGER.debugFailureToCreatePrincipal(roleName, var4);
         }
      }

   }

   private void parseRole(String dn, RoleGroup roleGroup) {
      StringTokenizer st = new StringTokenizer(dn, ",");

      while(st != null && st.hasMoreTokens()) {
         String keyVal = st.nextToken();
         if (keyVal.indexOf(this.roleNameAttributeID) > -1) {
            StringTokenizer kst = new StringTokenizer(keyVal, "=");
            kst.nextToken();
            this.addRole(kst.nextToken(), roleGroup);
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
