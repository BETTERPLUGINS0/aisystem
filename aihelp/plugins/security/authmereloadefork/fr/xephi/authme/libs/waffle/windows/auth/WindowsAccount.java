package fr.xephi.authme.libs.waffle.windows.auth;

import java.io.Serializable;

public class WindowsAccount implements Serializable {
   private static final long serialVersionUID = 1L;
   private final String sidString;
   private final String fqn;
   private final String name;
   private final String domain;

   public WindowsAccount(IWindowsAccount account) {
      this.sidString = account.getSidString();
      this.fqn = account.getFqn();
      this.name = account.getName();
      this.domain = account.getDomain();
   }

   public String getSidString() {
      return this.sidString;
   }

   public String getFqn() {
      return this.fqn;
   }

   public String getName() {
      return this.name;
   }

   public String getDomain() {
      return this.domain;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else {
         return !(o instanceof WindowsAccount) ? false : ((WindowsAccount)o).getSidString().equals(this.getSidString());
      }
   }

   public int hashCode() {
      return this.getSidString().hashCode();
   }
}
