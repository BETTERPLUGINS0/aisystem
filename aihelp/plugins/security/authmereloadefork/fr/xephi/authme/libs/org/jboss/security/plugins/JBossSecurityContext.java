package fr.xephi.authme.libs.org.jboss.security.plugins;

import fr.xephi.authme.libs.org.jboss.security.AuthenticationManager;
import fr.xephi.authme.libs.org.jboss.security.AuthorizationManager;
import fr.xephi.authme.libs.org.jboss.security.ISecurityManagement;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.RunAs;
import fr.xephi.authme.libs.org.jboss.security.SecurityContext;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextFactory;
import fr.xephi.authme.libs.org.jboss.security.SecurityContextUtil;
import fr.xephi.authme.libs.org.jboss.security.SecurityManagerLocator;
import fr.xephi.authme.libs.org.jboss.security.SubjectInfo;
import fr.xephi.authme.libs.org.jboss.security.audit.AuditManager;
import fr.xephi.authme.libs.org.jboss.security.auth.callback.JBossCallbackHandler;
import fr.xephi.authme.libs.org.jboss.security.identitytrust.IdentityTrustManager;
import fr.xephi.authme.libs.org.jboss.security.mapping.MappingManager;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import org.jboss.logging.Logger;

public class JBossSecurityContext implements SecurityContext, SecurityManagerLocator {
   private static final long serialVersionUID = 1L;
   private static final RuntimePermission getDataPermission = new RuntimePermission(JBossSecurityContext.class.getName() + ".getData");
   private static final RuntimePermission getSubjectInfoPermission = new RuntimePermission(JBossSecurityContext.class.getName() + ".getSubjectInfo");
   private static final RuntimePermission setRolesPermission = new RuntimePermission(JBossSecurityContext.class.getName() + ".setRolesPermission");
   private static final RuntimePermission setRunAsPermission = new RuntimePermission(JBossSecurityContext.class.getName() + ".setRunAsPermission");
   private static final RuntimePermission setSubjectInfoPermission = new RuntimePermission(JBossSecurityContext.class.getName() + ".setSubjectInfo");
   private static final RuntimePermission getSecurityManagementPermission = new RuntimePermission(JBossSecurityContext.class.getName() + ".getSecurityManagement");
   private static final RuntimePermission setSecurityManagementPermission = new RuntimePermission(JBossSecurityContext.class.getName() + ".setSecurityManagement");
   private static final RuntimePermission setSecurityDomainPermission = new RuntimePermission(JBossSecurityContext.class.getName() + ".setSecurityDomain");
   protected static final Logger log = Logger.getLogger(JBossSecurityContext.class);
   protected boolean trace;
   protected Map<String, Object> contextData;
   protected String securityDomain;
   protected SubjectInfo subjectInfo;
   protected RunAs incomingRunAs;
   protected RunAs outgoingRunAs;
   protected ISecurityManagement iSecurityManagement;
   protected transient CallbackHandler callbackHandler;
   protected transient SecurityContextUtil util;

   public JBossSecurityContext(String securityDomain) {
      this.trace = log.isTraceEnabled();
      this.contextData = new HashMap();
      this.securityDomain = "other";
      this.subjectInfo = null;
      this.incomingRunAs = null;
      this.outgoingRunAs = null;
      this.callbackHandler = new JBossCallbackHandler();
      this.util = null;
      this.securityDomain = securityDomain;
      if (this.callbackHandler == null) {
         this.callbackHandler = new JBossCallbackHandler();
      }

      this.iSecurityManagement = new DefaultSecurityManagement(this.callbackHandler);
      this.util = this.getUtil();
      this.util.createSubjectInfo((Principal)null, (Object)null, (Subject)null);
   }

   public ISecurityManagement getSecurityManagement() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(getSecurityManagementPermission);
      }

      return this.iSecurityManagement;
   }

   public void setSecurityManagement(ISecurityManagement securityManagement) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(setSecurityManagementPermission);
      }

      if (securityManagement == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("securityManagement");
      } else {
         this.iSecurityManagement = securityManagement;
      }
   }

   public Map<String, Object> getData() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(getDataPermission);
      }

      return this.contextData;
   }

   public String getSecurityDomain() {
      return this.securityDomain;
   }

   public void setSecurityDomain(String securityDomain) {
      SecurityManager manager = System.getSecurityManager();
      if (manager != null) {
         manager.checkPermission(setSecurityDomainPermission);
      }

      if (securityDomain == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("securityDomain");
      } else {
         this.securityDomain = securityDomain;
      }
   }

   public SubjectInfo getSubjectInfo() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(getSubjectInfoPermission);
      }

      return this.subjectInfo;
   }

   public RunAs getIncomingRunAs() {
      return this.incomingRunAs;
   }

   public void setIncomingRunAs(RunAs runAs) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(setRunAsPermission);
      }

      this.incomingRunAs = runAs;
   }

   public RunAs getOutgoingRunAs() {
      return this.outgoingRunAs;
   }

   public void setOutgoingRunAs(RunAs runAs) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(setRunAsPermission);
      }

      this.outgoingRunAs = runAs;
   }

   public SecurityContextUtil getUtil() {
      if (this.util == null) {
         try {
            this.util = SecurityContextFactory.createUtil(this);
         } catch (Exception var2) {
            throw new IllegalStateException(var2);
         }
      }

      return this.util;
   }

   public AuditManager getAuditManager() {
      return this.iSecurityManagement.getAuditManager(this.securityDomain);
   }

   public AuthenticationManager getAuthenticationManager() {
      return this.iSecurityManagement.getAuthenticationManager(this.securityDomain);
   }

   public AuthorizationManager getAuthorizationManager() {
      return this.iSecurityManagement.getAuthorizationManager(this.securityDomain);
   }

   public IdentityTrustManager getIdentityTrustManager() {
      return this.iSecurityManagement.getIdentityTrustManager(this.securityDomain);
   }

   public MappingManager getMappingManager() {
      return this.iSecurityManagement.getMappingManager(this.securityDomain);
   }

   public void setSubjectInfo(SubjectInfo si) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(setSubjectInfoPermission);
      }

      this.subjectInfo = si;
   }

   public void setRoles(Group roles, boolean replace) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(setRolesPermission);
      }

      Group mergedRoles = roles;
      if (!replace) {
         mergedRoles = this.mergeGroups((Group)this.contextData.get("Roles"), roles);
      }

      this.contextData.put("Roles", mergedRoles);
   }

   private Group mergeGroups(Group a, Group b) {
      Group newGroup = b;
      if (a != null) {
         Enumeration en = a.members();

         while(en.hasMoreElements()) {
            newGroup.addMember((Principal)en.nextElement());
         }
      }

      return newGroup;
   }

   public void setCallbackHandler(CallbackHandler callbackHandler) {
      this.callbackHandler = callbackHandler;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("[").append(this.getClass().getCanonicalName()).append("()");
      builder.append(this.securityDomain).append(")]");
      return builder.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      JBossSecurityContext jsc = (JBossSecurityContext)super.clone();
      if (jsc != null) {
         HashMap<String, Object> cmap = (HashMap)this.contextData;
         jsc.contextData = (Map)cmap.clone();
      }

      return super.clone();
   }
}
