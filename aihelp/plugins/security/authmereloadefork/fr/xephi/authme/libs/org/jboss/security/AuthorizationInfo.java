package fr.xephi.authme.libs.org.jboss.security;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.Subject;

public class AuthorizationInfo {
   private static Set<Principal> emptySet = new HashSet();
   private ArrayList<AuthorizationInfo.PolicyEntry> policyMap = new ArrayList();

   public PermissionCollection getPermissions(Subject subject, CodeSource codesource) {
      PermissionCollection perms = new Permissions();
      Set<Principal> subjectPrincipals = emptySet;
      if (subject != null) {
         subjectPrincipals = subject.getPrincipals();
      }

      for(int n = 0; n < this.policyMap.size(); ++n) {
         AuthorizationInfo.PolicyEntry entry = (AuthorizationInfo.PolicyEntry)this.policyMap.get(n);
         if (entry.implies(codesource, subjectPrincipals)) {
            entry.getPermissions(perms);
         }
      }

      return perms;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer("permissions:");
      return buffer.toString();
   }

   public void grant(CodeSource cs, ArrayList<Permission> permissions) {
      this.grant(cs, permissions, (Principal[])null);
   }

   public void grant(CodeSource cs, ArrayList<Permission> permissions, Principal[] principals) {
      AuthorizationInfo.PolicyEntry entry = new AuthorizationInfo.PolicyEntry(cs, principals, permissions);
      this.policyMap.add(entry);
   }

   static class PolicyEntry {
      private CodeSource cs;
      private Principal[] principals;
      private ArrayList<Permission> permissions;

      PolicyEntry(CodeSource cs, Principal[] principals, ArrayList<Permission> permissions) {
         this.cs = cs;
         this.principals = principals;
         this.permissions = permissions;
      }

      public void getPermissions(PermissionCollection perms) {
         int length = this.permissions == null ? 0 : this.permissions.size();

         for(int n = 0; n < length; ++n) {
            Permission permission = (Permission)this.permissions.get(n);
            perms.add(permission);
         }

      }

      public boolean implies(CodeSource codesrc, Set<Principal> subjectPrincipals) {
         boolean implies = false;
         if (this.cs == codesrc) {
            implies = true;
         } else if (this.cs != null && codesrc != null && this.cs.implies(codesrc)) {
            implies = true;
         }

         if (implies && this.principals != null) {
            for(int p = 0; p < this.principals.length; ++p) {
               if (!subjectPrincipals.contains(this.principals[p])) {
                  implies = false;
                  break;
               }
            }
         }

         return implies;
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof AuthorizationInfo.PolicyEntry)) {
            return false;
         } else {
            AuthorizationInfo.PolicyEntry key = (AuthorizationInfo.PolicyEntry)obj;
            boolean equals = this.cs == key.cs;
            if (!equals) {
               if (this.cs != null && key.cs != null) {
                  equals = this.cs.equals(key.cs);
               }

               if (equals) {
                  if (this.principals != null && key.principals != null && this.principals.length == key.principals.length) {
                     for(int p = 0; p < this.principals.length; ++p) {
                        if (!this.principals[p].equals(key.principals[p])) {
                           equals = false;
                           break;
                        }
                     }
                  } else if (this.principals != null || key.principals != null) {
                     equals = false;
                  }
               }
            }

            return equals;
         }
      }

      public int hashCode() {
         int hashCode = 0;
         if (this.cs != null) {
            hashCode = this.cs.hashCode();
         }

         int length = this.principals == null ? 0 : this.principals.length;

         for(int p = 0; p < length; ++p) {
            hashCode += this.principals[p].hashCode();
         }

         return hashCode;
      }

      public String toString() {
         StringBuffer buffer = new StringBuffer();
         buffer.append("cs=");
         buffer.append(this.cs);
         buffer.append("; principals=");

         for(int p = 0; this.principals != null && p < this.principals.length; ++p) {
            buffer.append(this.principals[p]);
         }

         buffer.append("; permissions=");
         buffer.append(this.permissions);
         return buffer.toString();
      }
   }
}
