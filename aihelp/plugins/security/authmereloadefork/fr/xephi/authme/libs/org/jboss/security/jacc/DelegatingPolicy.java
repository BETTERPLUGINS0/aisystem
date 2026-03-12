package fr.xephi.authme.libs.org.jboss.security.jacc;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.jacc.EJBMethodPermission;
import javax.security.jacc.EJBRoleRefPermission;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.security.jacc.WebResourcePermission;
import javax.security.jacc.WebRoleRefPermission;
import javax.security.jacc.WebUserDataPermission;

public class DelegatingPolicy extends Policy {
   private static DelegatingPolicy instance;
   private Policy delegate;
   private ConcurrentHashMap<String, ContextPolicy> activePolicies;
   private ConcurrentHashMap<String, ContextPolicy> openPolicies;
   private DelegatingPolicy.PolicyProxy policyProxy;
   private Class<?>[] externalPermissionTypes;

   public static synchronized DelegatingPolicy getInstance() {
      if (instance == null) {
         instance = new DelegatingPolicy();
      }

      return instance;
   }

   public DelegatingPolicy() {
      this((Policy)null);
   }

   public DelegatingPolicy(Policy delegate) {
      this.activePolicies = new ConcurrentHashMap();
      this.openPolicies = new ConcurrentHashMap();
      this.policyProxy = new DelegatingPolicy.PolicyProxy(this);
      this.externalPermissionTypes = new Class[0];
      if (delegate == null) {
         delegate = Policy.getPolicy();
      }

      this.delegate = delegate;
      if (instance == null) {
         instance = this;
      }

      Permission permission = new RuntimePermission("test");
      boolean var10000;
      if (!(permission instanceof EJBMethodPermission) && !(permission instanceof EJBRoleRefPermission) && !(permission instanceof WebResourcePermission) && !(permission instanceof WebRoleRefPermission) && !(permission instanceof WebUserDataPermission)) {
         var10000 = true;
      } else {
         var10000 = false;
      }

      Class<?> c = PolicyContext.class;
   }

   public Class<?>[] getExternalPermissionTypes() {
      return this.externalPermissionTypes;
   }

   public void setExternalPermissionTypes(Class<?>[] externalPermissionTypes) {
      if (externalPermissionTypes == null) {
         externalPermissionTypes = new Class[0];
      }

      this.externalPermissionTypes = externalPermissionTypes;
   }

   public PermissionCollection getPermissions(ProtectionDomain domain) {
      PermissionCollection pc = super.getPermissions(domain);
      PermissionCollection delegated = this.delegate.getPermissions(domain);
      Enumeration e = delegated.elements();

      while(e.hasMoreElements()) {
         Permission p = (Permission)e.nextElement();
         pc.add(p);
      }

      return pc;
   }

   public boolean implies(ProtectionDomain domain, Permission permission) {
      boolean isJaccPermission = permission instanceof EJBMethodPermission || permission instanceof EJBRoleRefPermission || permission instanceof WebResourcePermission || permission instanceof WebRoleRefPermission || permission instanceof WebUserDataPermission;
      boolean implied = false;
      if (!isJaccPermission && this.externalPermissionTypes.length > 0) {
         Class<?> pc = permission.getClass();

         for(int n = 0; n < this.externalPermissionTypes.length; ++n) {
            Class<?> epc = this.externalPermissionTypes[n];
            if (epc.isAssignableFrom(pc)) {
               isJaccPermission = true;
               break;
            }
         }
      }

      if (!isJaccPermission) {
         implied = this.delegate.implies(domain, permission);
      } else {
         String contextID = PolicyContext.getContextID();
         ContextPolicy contextPolicy = (ContextPolicy)this.activePolicies.get(contextID);
         if (contextPolicy != null) {
            implied = contextPolicy.implies(domain, permission);
         }
      }

      return implied;
   }

   public PermissionCollection getPermissions(CodeSource cs) {
      PermissionCollection pc = null;
      String contextID = PolicyContext.getContextID();
      if (contextID == null) {
         pc = this.delegate.getPermissions(cs);
      } else {
         ContextPolicy policy = (ContextPolicy)this.activePolicies.get(contextID);
         if (policy != null) {
            pc = policy.getPermissions();
            PermissionCollection delegatePerms = this.delegate.getPermissions(cs);
            Enumeration e = delegatePerms.elements();

            while(e.hasMoreElements()) {
               ((PermissionCollection)pc).add((Permission)e.nextElement());
            }
         } else {
            pc = this.delegate.getPermissions(cs);
         }
      }

      return (PermissionCollection)pc;
   }

   public void refresh() {
   }

   public String listContextPolicies() {
      StringBuffer tmp = new StringBuffer("<ActiveContextPolicies>");
      Iterator iter = this.activePolicies.keySet().iterator();

      String contextID;
      ContextPolicy cp;
      while(iter.hasNext()) {
         contextID = (String)iter.next();
         cp = (ContextPolicy)this.activePolicies.get(contextID);
         tmp.append(cp);
         tmp.append('\n');
      }

      tmp.append("</ActiveContextPolicies>");
      tmp.append("<OpenContextPolicies>");
      iter = this.openPolicies.keySet().iterator();

      while(iter.hasNext()) {
         contextID = (String)iter.next();
         cp = (ContextPolicy)this.openPolicies.get(contextID);
         tmp.append(cp);
         tmp.append('\n');
      }

      tmp.append("</OpenContextPolicies>");
      return tmp.toString();
   }

   public Policy getPolicyProxy() {
      return this.policyProxy;
   }

   synchronized ContextPolicy getContextPolicy(String contextID) throws PolicyContextException {
      ContextPolicy policy = (ContextPolicy)this.openPolicies.get(contextID);
      if (policy == null) {
         throw new PolicyContextException(PicketBoxMessages.MESSAGES.noPolicyContextForIdMessage(contextID));
      } else {
         return policy;
      }
   }

   synchronized void initPolicyConfiguration(String contextID, boolean remove) throws PolicyContextException {
      ContextPolicy policy = (ContextPolicy)this.activePolicies.remove(contextID);
      if (policy == null) {
         policy = (ContextPolicy)this.openPolicies.get(contextID);
      }

      if (policy == null) {
         policy = new ContextPolicy(contextID);
      }

      this.openPolicies.put(contextID, policy);
      if (remove) {
         policy.clear();
      }

   }

   void addToExcludedPolicy(String contextID, Permission permission) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      policy.addToExcludedPolicy(permission);
   }

   void addToExcludedPolicy(String contextID, PermissionCollection permissions) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      policy.addToExcludedPolicy(permissions);
   }

   void addToRole(String contextID, String roleName, Permission permission) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      policy.addToRole(roleName, permission);
   }

   void addToRole(String contextID, String roleName, PermissionCollection permissions) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      policy.addToRole(roleName, permissions);
   }

   void addToUncheckedPolicy(String contextID, Permission permission) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      policy.addToUncheckedPolicy(permission);
   }

   void addToUncheckedPolicy(String contextID, PermissionCollection permissions) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      policy.addToUncheckedPolicy(permissions);
   }

   void linkConfiguration(String contextID, PolicyConfiguration link) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      ContextPolicy linkPolicy = this.getContextPolicy(link.getContextID());
      policy.linkConfiguration(linkPolicy);
   }

   public void commit(String contextID) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      this.openPolicies.remove(contextID);
      this.activePolicies.put(contextID, policy);
      policy.commit();
   }

   public void delete(String contextID) throws PolicyContextException {
      ContextPolicy policy = (ContextPolicy)this.activePolicies.remove(contextID);
      if (policy == null) {
         policy = (ContextPolicy)this.openPolicies.remove(contextID);
      }

      if (policy != null) {
         policy.delete();
      }

   }

   void removeExcludedPolicy(String contextID) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      policy.removeExcludedPolicy();
   }

   void removeRole(String contextID, String roleName) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      policy.removeRole(roleName);
   }

   void removeUncheckedPolicy(String contextID) throws PolicyContextException {
      ContextPolicy policy = this.getContextPolicy(contextID);
      policy.removeUncheckedPolicy();
   }

   private static class PolicyProxy extends Policy {
      private Policy delegate;

      PolicyProxy(Policy delegate) {
         this.delegate = delegate;
      }

      public void refresh() {
         this.delegate.refresh();
      }

      public PermissionCollection getPermissions(CodeSource codesource) {
         return this.delegate.getPermissions(codesource);
      }

      public boolean implies(ProtectionDomain domain, Permission permission) {
         return this.delegate.implies(domain, permission);
      }

      public PermissionCollection getPermissions(ProtectionDomain domain) {
         return this.delegate.getPermissions(domain);
      }
   }
}
