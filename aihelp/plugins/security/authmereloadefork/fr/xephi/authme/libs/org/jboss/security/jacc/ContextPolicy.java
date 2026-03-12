package fr.xephi.authme.libs.org.jboss.security.jacc;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import javax.security.jacc.PolicyContextException;

public class ContextPolicy {
   private String contextID;
   private Permissions excludedPermissions = new Permissions();
   private Permissions uncheckedPermissions = new Permissions();
   private HashMap<String, Permissions> rolePermissions = new HashMap();
   private static final String ANY_AUTHENTICATED_USER_ROLE = "**";

   ContextPolicy(String contextID) {
      this.contextID = contextID;
   }

   Permissions getPermissions() {
      Permissions perms = new Permissions();
      Enumeration eter = this.uncheckedPermissions.elements();

      while(eter.hasMoreElements()) {
         Permission p = (Permission)eter.nextElement();
         perms.add(p);
      }

      Iterator iter = this.rolePermissions.values().iterator();

      while(iter.hasNext()) {
         Permissions rp = (Permissions)iter.next();
         eter = rp.elements();

         while(eter.hasMoreElements()) {
            Permission p = (Permission)eter.nextElement();
            perms.add(p);
         }
      }

      return perms;
   }

   boolean implies(ProtectionDomain domain, Permission permission) {
      boolean implied = false;
      if (this.excludedPermissions.implies(permission)) {
         PicketBoxLogger.LOGGER.traceImpliesMatchesExcludedSet(permission);
         return false;
      } else if (this.uncheckedPermissions.implies(permission)) {
         PicketBoxLogger.LOGGER.traceImpliesMatchesUncheckedSet(permission);
         return true;
      } else {
         Principal[] principals = domain.getPrincipals();
         int length = principals != null ? principals.length : 0;
         ArrayList<String> principalNames = new ArrayList();

         int n;
         for(n = 0; n < length; ++n) {
            Principal p = principals[n];
            if (p instanceof Group) {
               Group g = (Group)p;
               Enumeration iter = g.members();

               while(iter.hasMoreElements()) {
                  p = (Principal)iter.nextElement();
                  String name = p.getName();
                  principalNames.add(name);
               }
            } else {
               String name = p.getName();
               principalNames.add(name);
            }
         }

         if (principalNames.size() == 0) {
            PicketBoxLogger.LOGGER.traceNoPrincipalsInProtectionDomain(domain);
         }

         principalNames.add("**");
         PicketBoxLogger.LOGGER.traceProtectionDomainPrincipals(principalNames);

         for(n = 0; !implied && n < principalNames.size(); ++n) {
            String name = (String)principalNames.get(n);
            Permissions perms = (Permissions)this.rolePermissions.get(name);
            PicketBoxLogger.LOGGER.debugImpliesParameters(name, perms);
            if (perms != null) {
               implied = perms.implies(permission);
               PicketBoxLogger.LOGGER.debugImpliesResult(implied);
            }
         }

         return implied;
      }
   }

   void clear() {
      this.excludedPermissions = new Permissions();
      this.uncheckedPermissions = new Permissions();
      this.rolePermissions.clear();
   }

   void addToExcludedPolicy(Permission permission) throws PolicyContextException {
      this.excludedPermissions.add(permission);
   }

   void addToExcludedPolicy(PermissionCollection permissions) throws PolicyContextException {
      Enumeration iter = permissions.elements();

      while(iter.hasMoreElements()) {
         Permission p = (Permission)iter.nextElement();
         this.excludedPermissions.add(p);
      }

   }

   void addToRole(String roleName, Permission permission) throws PolicyContextException {
      Permissions perms = (Permissions)this.rolePermissions.get(roleName);
      if (perms == null) {
         perms = new Permissions();
         this.rolePermissions.put(roleName, perms);
      }

      perms.add(permission);
   }

   void addToRole(String roleName, PermissionCollection permissions) throws PolicyContextException {
      Permissions perms = (Permissions)this.rolePermissions.get(roleName);
      if (perms == null) {
         perms = new Permissions();
         this.rolePermissions.put(roleName, perms);
      }

      Enumeration iter = permissions.elements();

      while(iter.hasMoreElements()) {
         Permission p = (Permission)iter.nextElement();
         perms.add(p);
      }

   }

   void addToUncheckedPolicy(Permission permission) throws PolicyContextException {
      this.uncheckedPermissions.add(permission);
   }

   void addToUncheckedPolicy(PermissionCollection permissions) throws PolicyContextException {
      Enumeration iter = permissions.elements();

      while(iter.hasMoreElements()) {
         Permission p = (Permission)iter.nextElement();
         this.uncheckedPermissions.add(p);
      }

   }

   void commit() throws PolicyContextException {
   }

   void delete() throws PolicyContextException {
      this.clear();
   }

   String getContextID() throws PolicyContextException {
      return this.contextID;
   }

   void linkConfiguration(ContextPolicy link) throws PolicyContextException {
   }

   void removeExcludedPolicy() throws PolicyContextException {
      this.excludedPermissions = new Permissions();
   }

   void removeRole(String roleName) throws PolicyContextException {
      if ("*".equals(roleName) && !this.rolePermissions.containsKey("*")) {
         this.rolePermissions.clear();
      } else {
         this.rolePermissions.remove(roleName);
      }

   }

   void removeUncheckedPolicy() throws PolicyContextException {
      this.uncheckedPermissions = new Permissions();
   }

   Permissions getPermissionsForRole(String role) {
      return (Permissions)this.rolePermissions.get(role);
   }

   public String toString() {
      StringBuffer tmp = new StringBuffer("<ContextPolicy contextID='");
      tmp.append(this.contextID);
      tmp.append("'>\n");
      tmp.append("\t<ExcludedPermissions>\n");
      Enumeration iter = this.excludedPermissions.elements();

      Permission p;
      while(iter.hasMoreElements()) {
         p = (Permission)iter.nextElement();
         tmp.append("<Permission type='");
         tmp.append(p.getClass());
         tmp.append("' name='");
         tmp.append(p.getName());
         tmp.append("' actions='");
         tmp.append(p.getActions());
         tmp.append("' />\n");
      }

      tmp.append("\t</ExcludedPermissions>\n");
      tmp.append("\t<UncheckedPermissions>\n");
      iter = this.uncheckedPermissions.elements();

      while(iter.hasMoreElements()) {
         p = (Permission)iter.nextElement();
         tmp.append("<Permission type='");
         tmp.append(p.getClass());
         tmp.append(" name='");
         tmp.append(p.getName());
         tmp.append("' actions='");
         tmp.append(p.getActions());
         tmp.append("' />\n");
      }

      tmp.append("\t</UncheckedPermissions>\n");
      tmp.append("\t<RolePermssions>\n");
      Iterator roles = this.rolePermissions.keySet().iterator();

      while(roles.hasNext()) {
         String role = (String)roles.next();
         Permissions perms = (Permissions)this.rolePermissions.get(role);
         iter = perms.elements();
         tmp.append("\t\t<Role name='" + role + "'>\n");

         while(iter.hasMoreElements()) {
            Permission p = (Permission)iter.nextElement();
            tmp.append("<Permission type='");
            tmp.append(p.getClass());
            tmp.append(" name='");
            tmp.append(p.getName());
            tmp.append("' actions='");
            tmp.append(p.getActions());
            tmp.append("' />\n");
         }

         tmp.append("\t\t</Role>\n");
      }

      tmp.append("\t</RolePermssions>");
      tmp.append("</ContextPolicy>\n");
      return tmp.toString();
   }
}
