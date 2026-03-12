package fr.xephi.authme.libs.org.jboss.security.config;

import java.util.ArrayList;
import java.util.List;
import javax.security.auth.AuthPermission;

public abstract class BaseSecurityInfo<T> {
   public static final AuthPermission GET_CONFIG_ENTRY_PERM = new AuthPermission("getLoginConfiguration");
   public static final AuthPermission SET_CONFIG_ENTRY_PERM = new AuthPermission("setLoginConfiguration");
   protected String name;
   protected ArrayList<T> moduleEntries = new ArrayList();
   protected String jbossModuleName;

   public BaseSecurityInfo() {
   }

   public BaseSecurityInfo(String name) {
      this.name = name;
   }

   public void add(T ame) {
      this.moduleEntries.add(ame);
   }

   public void add(List<? extends T> moduleEntries) {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(SET_CONFIG_ENTRY_PERM);
      }

      this.moduleEntries.addAll(moduleEntries);
   }

   public List<T> getModuleEntries() {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      }

      return this.moduleEntries;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getJBossModuleName() {
      return this.jbossModuleName;
   }

   public void setJBossModuleName(String jbossModuleName) {
      this.jbossModuleName = jbossModuleName;
   }

   protected abstract BaseSecurityInfo<T> create(String var1);

   public BaseSecurityInfo<T> merge(BaseSecurityInfo<T> bi) {
      if (bi == null) {
         return this;
      } else {
         List<T> al = new ArrayList();
         al.addAll(bi.getModuleEntries());
         al.addAll(this.moduleEntries);
         BaseSecurityInfo<T> mergedBAI = this.create(this.name);
         mergedBAI.add((List)al);
         return mergedBAI;
      }
   }
}
