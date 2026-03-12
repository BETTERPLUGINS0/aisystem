package fr.xephi.authme.libs.org.jboss.security.mapping.providers.attribute;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.Util;
import fr.xephi.authme.libs.org.jboss.security.identity.Attribute;
import fr.xephi.authme.libs.org.jboss.security.identity.AttributeFactory;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingProvider;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingResult;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import javax.management.ObjectName;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;

public class LdapAttributeMappingProvider implements MappingProvider<List<Attribute<String>>> {
   private Map<String, Object> options;
   protected int searchTimeLimit = 10000;
   private static final String BIND_DN = "bindDN";
   private static final String BIND_CREDENTIAL = "bindCredential";
   private static final String BASE_CTX_DN = "baseCtxDN";
   private static final String BASE_FILTER_OPT = "baseFilter";
   private static final String SEARCH_TIME_LIMIT_OPT = "searchTimeLimit";
   private static final String ATTRIBUTE_LIST_OPT = "attributeList";
   private static final String SECURITY_DOMAIN_OPT = "jaasSecurityDomain";
   private MappingResult<List<Attribute<String>>> mappingResult;

   public void init(Map<String, Object> options) {
      this.options = options;
   }

   public void performMapping(Map<String, Object> map, List<Attribute<String>> mappedObject) {
      List<Attribute<String>> attributeList = new ArrayList();
      Principal principal = (Principal)map.get("Principal");
      if (principal != null) {
         String user = principal.getName();
         String bindDN = (String)this.options.get("bindDN");
         if (bindDN == null || bindDN.length() == 0) {
            PicketBoxLogger.LOGGER.traceBindDNNotFound();
            return;
         }

         String bindCredential = (String)this.options.get("bindCredential");
         if (Util.isPasswordCommand(bindCredential)) {
            try {
               bindCredential = new String(Util.loadPassword(bindCredential));
            } catch (Exception var34) {
               PicketBoxLogger.LOGGER.errorDecryptingBindCredential(var34);
               return;
            }
         }

         String securityDomain = (String)this.options.get("jaasSecurityDomain");
         if (securityDomain != null) {
            try {
               ObjectName serviceName = new ObjectName(securityDomain);
               char[] tmp = MappingProvidersDecodeAction.decode(bindCredential, serviceName);
               bindCredential = new String(tmp);
            } catch (Exception var33) {
               PicketBoxLogger.LOGGER.errorDecryptingBindCredential(var33);
               return;
            }
         }

         ClassLoader currentTCCL = SecurityActions.getContextClassLoader();

         InitialLdapContext ctx;
         try {
            if (currentTCCL != null) {
               SecurityActions.setContextClassLoader((ClassLoader)null);
            }

            ctx = this.constructInitialLdapContext(bindDN, bindCredential);
         } catch (NamingException var37) {
            throw new RuntimeException(var37);
         }

         String timeLimit = (String)this.options.get("searchTimeLimit");
         if (timeLimit != null) {
            try {
               this.searchTimeLimit = Integer.parseInt(timeLimit);
            } catch (NumberFormatException var32) {
               PicketBoxLogger.LOGGER.debugFailureToParseNumberProperty("searchTimeLimit", (long)this.searchTimeLimit);
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
         String attributePattern = (String)this.options.get("attributeList");
         String[] neededAttributes = this.getNeededAttributes(attributePattern);
         constraints.setReturningAttributes(neededAttributes);
         NamingEnumeration<SearchResult> results = null;
         Object[] filterArgs = new Object[]{user};

         try {
            if (baseDN == null) {
               throw PicketBoxMessages.MESSAGES.invalidNullArgument("baseCtxDN");
            }

            results = ctx.search(baseDN, baseFilter, filterArgs, constraints);
            if (!results.hasMore()) {
               results.close();
               throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
            }

            SearchResult sr = (SearchResult)results.next();
            String name = sr.getName();
            String userDN = null;
            if (!sr.isRelative()) {
               throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);
            }

            userDN = name + "," + baseDN;
            results.close();
            filterArgs = new Object[]{user, userDN};
            results = ctx.search(userDN, baseFilter, filterArgs, constraints);

            try {
               while(results.hasMore()) {
                  sr = (SearchResult)results.next();
                  Attributes attributes = sr.getAttributes();
                  NamingEnumeration ne = attributes.getAll();

                  while(ne != null && ne.hasMoreElements()) {
                     javax.naming.directory.Attribute ldapAtt = (javax.naming.directory.Attribute)ne.next();
                     if ("mail".equalsIgnoreCase(ldapAtt.getID())) {
                        attributeList.add(AttributeFactory.createEmailAddress((String)ldapAtt.get()));
                     } else {
                        attributeList.add(AttributeFactory.createAttribute(ldapAtt.getID(), (String)ldapAtt.get()));
                     }
                  }
               }
            } finally {
               if (results != null) {
                  results.close();
               }

               if (ctx != null) {
                  ctx.close();
               }

               if (currentTCCL != null) {
                  SecurityActions.setContextClassLoader(currentTCCL);
               }

            }
         } catch (NamingException var36) {
            PicketBoxLogger.LOGGER.debugIgnoredException(var36);
            return;
         }

         results = null;
      }

      mappedObject.addAll(attributeList);
      this.mappingResult.setMappedObject(mappedObject);
   }

   public void setMappingResult(MappingResult<List<Attribute<String>>> result) {
      this.mappingResult = result;
   }

   public boolean supports(Class<?> clazz) {
      return Attribute.class.isAssignableFrom(clazz);
   }

   private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException {
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

   private String[] getNeededAttributes(String commaSeparatedList) {
      ArrayList<String> arrayList = new ArrayList();
      if (commaSeparatedList != null) {
         StringTokenizer st = new StringTokenizer(commaSeparatedList, ",");

         while(st.hasMoreTokens()) {
            arrayList.add(st.nextToken());
         }
      }

      String[] strArr = new String[arrayList.size()];
      return (String[])arrayList.toArray(strArr);
   }
}
