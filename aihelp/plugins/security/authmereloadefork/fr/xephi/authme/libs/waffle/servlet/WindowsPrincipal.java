package fr.xephi.authme.libs.waffle.servlet;

import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAccount;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import fr.xephi.authme.libs.waffle.windows.auth.PrincipalFormat;
import fr.xephi.authme.libs.waffle.windows.auth.WindowsAccount;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowsPrincipal implements Principal, Serializable {
   private static final long serialVersionUID = 1L;
   private final String fqn;
   private final byte[] sid;
   private final String sidString;
   private final List<String> roles;
   private transient IWindowsIdentity identity;
   private final Map<String, WindowsAccount> groups;

   public WindowsPrincipal(IWindowsIdentity windowsIdentity) {
      this(windowsIdentity, PrincipalFormat.FQN, PrincipalFormat.FQN);
   }

   public WindowsPrincipal(IWindowsIdentity windowsIdentity, PrincipalFormat principalFormat, PrincipalFormat roleFormat) {
      this.identity = windowsIdentity;
      this.fqn = windowsIdentity.getFqn();
      this.sid = windowsIdentity.getSid();
      this.sidString = windowsIdentity.getSidString();
      this.groups = getGroups(windowsIdentity.getGroups());
      this.roles = getRoles(windowsIdentity, principalFormat, roleFormat);
   }

   private static List<String> getRoles(IWindowsIdentity windowsIdentity, PrincipalFormat principalFormat, PrincipalFormat roleFormat) {
      List<String> roles = new ArrayList();
      roles.addAll(getPrincipalNames(windowsIdentity, principalFormat));
      IWindowsAccount[] var4 = windowsIdentity.getGroups();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         IWindowsAccount group = var4[var6];
         roles.addAll(getRoleNames(group, roleFormat));
      }

      return roles;
   }

   private static Map<String, WindowsAccount> getGroups(IWindowsAccount[] groups) {
      Map<String, WindowsAccount> groupMap = new HashMap();
      IWindowsAccount[] var2 = groups;
      int var3 = groups.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         IWindowsAccount group = var2[var4];
         groupMap.put(group.getFqn(), new WindowsAccount(group));
      }

      return groupMap;
   }

   public byte[] getSid() {
      return (byte[])this.sid.clone();
   }

   public String getSidString() {
      return this.sidString;
   }

   public Map<String, WindowsAccount> getGroups() {
      return this.groups;
   }

   private static List<String> getRoleNames(IWindowsAccount group, PrincipalFormat principalFormat) {
      List<String> principals = new ArrayList();
      switch(principalFormat) {
      case FQN:
         principals.add(group.getFqn());
         break;
      case SID:
         principals.add(group.getSidString());
         break;
      case BOTH:
         principals.add(group.getFqn());
         principals.add(group.getSidString());
      case NONE:
      }

      return principals;
   }

   private static List<String> getPrincipalNames(IWindowsIdentity windowsIdentity, PrincipalFormat principalFormat) {
      List<String> principals = new ArrayList();
      switch(principalFormat) {
      case FQN:
         principals.add(windowsIdentity.getFqn());
         break;
      case SID:
         principals.add(windowsIdentity.getSidString());
         break;
      case BOTH:
         principals.add(windowsIdentity.getFqn());
         principals.add(windowsIdentity.getSidString());
      case NONE:
      }

      return principals;
   }

   public String getRolesString() {
      return String.join(", ", this.roles);
   }

   public boolean hasRole(String role) {
      return this.roles.contains(role);
   }

   public String getName() {
      return this.fqn;
   }

   public IWindowsIdentity getIdentity() {
      return this.identity;
   }

   public String toString() {
      return this.getName();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return o instanceof WindowsPrincipal ? this.getName().equals(((WindowsPrincipal)o).getName()) : false;
      }
   }

   public int hashCode() {
      return this.getName().hashCode();
   }
}
