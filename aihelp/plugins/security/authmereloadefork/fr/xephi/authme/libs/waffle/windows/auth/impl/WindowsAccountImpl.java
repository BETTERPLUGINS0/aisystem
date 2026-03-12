package fr.xephi.authme.libs.waffle.windows.auth.impl;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Secur32Util;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAccount;

public class WindowsAccountImpl implements IWindowsAccount {
   private final Advapi32Util.Account account;

   public WindowsAccountImpl(Advapi32Util.Account newAccount) {
      this.account = newAccount;
   }

   public WindowsAccountImpl(String userName) {
      this(userName, (String)null);
   }

   public WindowsAccountImpl(String accountName, String systemName) {
      this(Advapi32Util.getAccountByName(systemName, accountName));
   }

   public static String getCurrentUsername() {
      return Secur32Util.getUserNameEx(2);
   }

   public String getDomain() {
      return this.account.domain;
   }

   public String getFqn() {
      return this.account.fqn;
   }

   public String getName() {
      return this.account.name;
   }

   public String getSidString() {
      return this.account.sidString;
   }
}
