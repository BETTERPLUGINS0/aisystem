package fr.xephi.authme.libs.org.picketbox.plugins;

import fr.xephi.authme.libs.org.jboss.security.AuthenticationManager;
import fr.xephi.authme.libs.org.jboss.security.AuthorizationManager;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SimplePrincipal;
import fr.xephi.authme.libs.org.jboss.security.annotation.Authentication;
import fr.xephi.authme.libs.org.jboss.security.annotation.Authorization;
import fr.xephi.authme.libs.org.jboss.security.annotation.Module;
import fr.xephi.authme.libs.org.jboss.security.annotation.ModuleOption;
import fr.xephi.authme.libs.org.jboss.security.annotation.SecurityAudit;
import fr.xephi.authme.libs.org.jboss.security.annotation.SecurityConfig;
import fr.xephi.authme.libs.org.jboss.security.annotation.SecurityDomain;
import fr.xephi.authme.libs.org.jboss.security.annotation.SecurityMapping;
import fr.xephi.authme.libs.org.jboss.security.audit.config.AuditProviderEntry;
import fr.xephi.authme.libs.org.jboss.security.auth.login.AuthenticationInfo;
import fr.xephi.authme.libs.org.jboss.security.authorization.AuthorizationException;
import fr.xephi.authme.libs.org.jboss.security.authorization.config.AuthorizationModuleEntry;
import fr.xephi.authme.libs.org.jboss.security.callbacks.SecurityContextCallbackHandler;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicy;
import fr.xephi.authme.libs.org.jboss.security.config.ApplicationPolicyRegistration;
import fr.xephi.authme.libs.org.jboss.security.config.AuditInfo;
import fr.xephi.authme.libs.org.jboss.security.config.AuthorizationInfo;
import fr.xephi.authme.libs.org.jboss.security.config.ControlFlag;
import fr.xephi.authme.libs.org.jboss.security.config.MappingInfo;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.mapping.config.MappingModuleEntry;
import fr.xephi.authme.libs.org.picketbox.config.PicketBoxConfiguration;
import fr.xephi.authme.libs.org.picketbox.core.authorization.resources.POJOResource;
import fr.xephi.authme.libs.org.picketbox.exceptions.PicketBoxProcessingException;
import fr.xephi.authme.libs.org.picketbox.factories.SecurityFactory;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

public class PicketBoxProcessor {
   private Principal principal = null;
   private Object credential = null;

   public void setSecurityInfo(String userName, Object credential) {
      this.principal = new SimplePrincipal(userName);
      this.credential = credential;
   }

   public Principal getCallerPrincipal() throws PicketBoxProcessingException {
      Principal principal = null;
      SecurityContext securityContext = null;

      try {
         securityContext = SecurityActions.getSecurityContext();
      } catch (PrivilegedActionException var4) {
         throw new PicketBoxProcessingException(var4.getCause());
      }

      if (securityContext != null) {
         principal = securityContext.getUtil().getUserPrincipal();
      }

      return principal;
   }

   public RoleGroup getCallerRoles() throws PicketBoxProcessingException {
      RoleGroup roleGroup = null;
      SecurityContext securityContext = null;

      try {
         securityContext = SecurityActions.getSecurityContext();
      } catch (PrivilegedActionException var4) {
         throw new PicketBoxProcessingException(var4.getCause());
      }

      if (securityContext != null) {
         roleGroup = securityContext.getUtil().getRoles();
      }

      return roleGroup;
   }

   public Subject getCallerSubject() throws PicketBoxProcessingException {
      Subject subject = new Subject();
      SecurityContext securityContext = null;

      try {
         securityContext = SecurityActions.getSecurityContext();
      } catch (PrivilegedActionException var4) {
         throw new PicketBoxProcessingException(var4.getCause());
      }

      if (securityContext != null) {
         subject = securityContext.getUtil().getSubject();
      }

      return subject;
   }

   public void process(Object pojo) throws LoginException, PicketBoxProcessingException {
      String securityDomain = "other";
      Class<?> objectClass = pojo.getClass();
      SecurityDomain securityDomainAnnotation = (SecurityDomain)objectClass.getAnnotation(SecurityDomain.class);
      if (securityDomainAnnotation != null) {
         securityDomain = securityDomainAnnotation.value();
      }

      SecurityFactory.prepare();

      try {
         boolean needAuthorization = false;
         SecurityConfig securityConfig = (SecurityConfig)objectClass.getAnnotation(SecurityConfig.class);
         Authentication authenticationAnnotation = (Authentication)objectClass.getAnnotation(Authentication.class);
         if (securityConfig == null && authenticationAnnotation == null) {
            throw PicketBoxMessages.MESSAGES.invalidSecurityAnnotationConfig();
         }

         if (securityConfig != null) {
            PicketBoxConfiguration idtrustConfig = new PicketBoxConfiguration();
            idtrustConfig.load(securityConfig.fileName());
         } else {
            ApplicationPolicyRegistration apr = (ApplicationPolicyRegistration)Configuration.getConfiguration();
            ApplicationPolicy aPolicy = new ApplicationPolicy(securityDomain);
            AuthenticationInfo authenticationInfo = this.getAuthenticationInfo(authenticationAnnotation, securityDomain);
            aPolicy.setAuthenticationInfo(authenticationInfo);
            Authorization authorizationAnnotation = (Authorization)objectClass.getAnnotation(Authorization.class);
            SecurityAudit auditAnnotation = (SecurityAudit)objectClass.getAnnotation(SecurityAudit.class);
            SecurityMapping mappingAnnotation = (SecurityMapping)objectClass.getAnnotation(SecurityMapping.class);
            if (authorizationAnnotation != null) {
               AuthorizationInfo authorizationInfo = this.getAuthorizationInfo(authorizationAnnotation, securityDomain);
               aPolicy.setAuthorizationInfo(authorizationInfo);
               needAuthorization = true;
            }

            if (auditAnnotation != null) {
               AuditInfo auditInfo = this.getAuditInfo(auditAnnotation, securityDomain);
               aPolicy.setAuditInfo(auditInfo);
            }

            if (mappingAnnotation != null) {
               MappingInfo mappingInfo = this.getMappingInfo(mappingAnnotation, securityDomain);
               List<MappingModuleEntry> entries = mappingInfo.getModuleEntries();
               Iterator i$ = entries.iterator();

               while(i$.hasNext()) {
                  MappingModuleEntry entry = (MappingModuleEntry)i$.next();
                  aPolicy.setMappingInfo(entry.getMappingModuleType(), mappingInfo);
               }
            }

            apr.addApplicationPolicy(securityDomain, aPolicy);
         }

         SecurityContext securityContext = SecurityActions.createSecurityContext(securityDomain);
         SecurityActions.setSecurityContext(securityContext);
         AuthenticationManager authMgr = SecurityFactory.getAuthenticationManager(securityDomain);
         Subject subject = new Subject();
         boolean valid = authMgr.isValid(this.principal, this.credential, subject);
         if (!valid) {
            throw new LoginException(PicketBoxMessages.MESSAGES.authenticationFailedMessage());
         }

         SecurityActions.register(securityContext, this.principal, this.credential, subject);
         AuthorizationManager authzMgr = SecurityFactory.getAuthorizationManager(securityDomain);
         SecurityContextCallbackHandler cbh = new SecurityContextCallbackHandler(securityContext);
         RoleGroup roles = authzMgr.getSubjectRoles(subject, cbh);
         if (roles == null) {
            throw new PicketBoxProcessingException(PicketBoxMessages.MESSAGES.nullRolesInSubjectMessage());
         }

         if (needAuthorization) {
            int permit = authzMgr.authorize(new POJOResource(pojo), subject, (RoleGroup)roles);
            if (permit != 1) {
               throw new AuthorizationException(PicketBoxMessages.MESSAGES.authorizationFailedMessage());
            }
         }
      } catch (PrivilegedActionException var23) {
         throw new PicketBoxProcessingException(var23.getCause());
      } catch (AuthorizationException var24) {
         throw new PicketBoxProcessingException(var24);
      } catch (Exception var25) {
         throw new PicketBoxProcessingException(var25);
      } finally {
         SecurityFactory.release();
      }

   }

   private MappingInfo getMappingInfo(SecurityMapping mappingAnnotation, String securityDomain) {
      MappingInfo mappingInfo = new MappingInfo(securityDomain);
      Module[] modules = mappingAnnotation.modules();
      if (modules != null) {
         Module[] arr$ = modules;
         int len$ = modules.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Module module = arr$[i$];
            String code = module.code().getCanonicalName();
            String type = module.type();
            Map<String, Object> map = new HashMap();
            ModuleOption[] options = module.options();
            if (options != null) {
               ModuleOption[] arr$ = options;
               int len$ = options.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  ModuleOption option = arr$[i$];
                  String key = option.key();
                  String value = option.value();
                  ModuleOption.VALUE_TYPE valueType = option.valueType();
                  if (key != null && key.length() > 0 && valueType == ModuleOption.VALUE_TYPE.JAVA_PROPERTIES) {
                     StringTokenizer st = new StringTokenizer(value, "=");
                     String prop1 = st.nextToken();
                     String prop2 = st.nextToken();
                     Properties properties = new Properties();
                     properties.put(prop1, prop2);
                     map.put(key, properties);
                  } else if (key != null && key.length() > 0) {
                     map.put(key, value);
                  }
               }
            }

            MappingModuleEntry entry = new MappingModuleEntry(code, map, type);
            mappingInfo.add(entry);
         }
      }

      return mappingInfo;
   }

   private AuditInfo getAuditInfo(SecurityAudit auditAnnotation, String securityDomain) {
      AuditInfo auditInfo = new AuditInfo(securityDomain);
      Module[] modules = auditAnnotation.modules();
      if (modules != null) {
         Module[] arr$ = modules;
         int len$ = modules.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Module module = arr$[i$];
            String code = module.code().getCanonicalName();
            Map<String, Object> map = new HashMap();
            ModuleOption[] options = module.options();
            if (options != null) {
               ModuleOption[] arr$ = options;
               int len$ = options.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  ModuleOption option = arr$[i$];
                  String key = option.key();
                  String value = option.value();
                  if (key != null && key.length() > 0) {
                     map.put(key, value);
                  }
               }
            }

            AuditProviderEntry entry = new AuditProviderEntry(code, map);
            auditInfo.add(entry);
         }
      }

      return auditInfo;
   }

   private AuthorizationInfo getAuthorizationInfo(Authorization authorizationAnnotation, String securityDomain) {
      AuthorizationInfo authorizationInfo = new AuthorizationInfo(securityDomain);
      Module[] modules = authorizationAnnotation.modules();
      if (modules != null) {
         Module[] arr$ = modules;
         int len$ = modules.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Module module = arr$[i$];
            String code = module.code().getCanonicalName();
            String flag = module.flag();
            Map<String, Object> map = new HashMap();
            ModuleOption[] options = module.options();
            if (options != null) {
               ModuleOption[] arr$ = options;
               int len$ = options.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  ModuleOption option = arr$[i$];
                  String key = option.key();
                  String value = option.value();
                  if (key != null && key.length() > 0) {
                     map.put(key, value);
                  }
               }
            }

            AuthorizationModuleEntry entry = new AuthorizationModuleEntry(code, map);
            entry.setControlFlag(ControlFlag.valueOf(flag));
            authorizationInfo.add(entry);
         }
      }

      return authorizationInfo;
   }

   private AuthenticationInfo getAuthenticationInfo(Authentication auth, String securityDomainName) {
      AuthenticationInfo authInfo = new AuthenticationInfo(securityDomainName);
      Module[] modules = auth.modules();
      if (modules != null) {
         Module[] arr$ = modules;
         int len$ = modules.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Module module = arr$[i$];
            String code = module.code().getCanonicalName();
            String flag = module.flag();
            Map<String, Object> map = new HashMap();
            ModuleOption[] options = module.options();
            if (options != null) {
               ModuleOption[] arr$ = options;
               int len$ = options.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  ModuleOption option = arr$[i$];
                  String key = option.key();
                  String value = option.value();
                  if (key != null && key.length() > 0) {
                     map.put(key, value);
                  }
               }
            }

            AppConfigurationEntry entry = new AppConfigurationEntry(code, this.getFlag(flag), map);
            authInfo.addAppConfigurationEntry(entry);
         }
      }

      return authInfo;
   }

   private LoginModuleControlFlag getFlag(String flag) {
      if ("REQUIRED".equalsIgnoreCase(flag)) {
         return LoginModuleControlFlag.REQUIRED;
      } else if ("REQUISITE".equalsIgnoreCase(flag)) {
         return LoginModuleControlFlag.REQUISITE;
      } else {
         return "SUFFICIENT".equalsIgnoreCase(flag) ? LoginModuleControlFlag.SUFFICIENT : LoginModuleControlFlag.OPTIONAL;
      }
   }
}
