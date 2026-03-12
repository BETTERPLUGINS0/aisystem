package fr.xephi.authme.libs.waffle.windows.auth.impl;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAccount;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsImpersonationContext;
import java.util.ArrayList;
import java.util.List;

public class WindowsIdentityImpl implements IWindowsIdentity {
   private final WinNT.HANDLE windowsIdentity;
   private Advapi32Util.Account[] userGroups;
   private Advapi32Util.Account windowsAccount;

   public WindowsIdentityImpl(WinNT.HANDLE newWindowsIdentity) {
      this.windowsIdentity = newWindowsIdentity;
   }

   private Advapi32Util.Account getWindowsAccount() {
      if (this.windowsAccount == null) {
         this.windowsAccount = Advapi32Util.getTokenAccount(this.windowsIdentity);
      }

      return this.windowsAccount;
   }

   private Advapi32Util.Account[] getUserGroups() {
      if (this.userGroups == null) {
         this.userGroups = Advapi32Util.getTokenGroups(this.windowsIdentity);
      }

      return (Advapi32Util.Account[])this.userGroups.clone();
   }

   public String getFqn() {
      return this.getWindowsAccount().fqn;
   }

   public IWindowsAccount[] getGroups() {
      Advapi32Util.Account[] groups = this.getUserGroups();
      List<IWindowsAccount> result = new ArrayList(groups.length);
      Advapi32Util.Account[] var3 = groups;
      int var4 = groups.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Advapi32Util.Account userGroup = var3[var5];
         WindowsAccountImpl account = new WindowsAccountImpl(userGroup);
         result.add(account);
      }

      return (IWindowsAccount[])result.toArray(new IWindowsAccount[0]);
   }

   public byte[] getSid() {
      return this.getWindowsAccount().sid;
   }

   public String getSidString() {
      return this.getWindowsAccount().sidString;
   }

   public void dispose() {
      if (this.windowsIdentity != null) {
         Kernel32.INSTANCE.CloseHandle(this.windowsIdentity);
      }

   }

   public IWindowsImpersonationContext impersonate() {
      return new WindowsIdentityImpersonationContextImpl(this.windowsIdentity);
   }

   public boolean isGuest() {
      Advapi32Util.Account[] var1 = this.getUserGroups();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Advapi32Util.Account userGroup = var1[var3];
         if (Advapi32Util.isWellKnownSid((byte[])userGroup.sid, 28)) {
            return true;
         }

         if (Advapi32Util.isWellKnownSid((byte[])userGroup.sid, 43)) {
            return true;
         }

         if (Advapi32Util.isWellKnownSid((byte[])userGroup.sid, 39)) {
            return true;
         }
      }

      return Advapi32Util.isWellKnownSid((byte[])this.getSid(), 13);
   }
}
