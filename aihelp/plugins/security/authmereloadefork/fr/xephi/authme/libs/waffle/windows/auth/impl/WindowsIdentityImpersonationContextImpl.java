package fr.xephi.authme.libs.waffle.windows.auth.impl;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsImpersonationContext;

public class WindowsIdentityImpersonationContextImpl implements IWindowsImpersonationContext {
   public WindowsIdentityImpersonationContextImpl(WinNT.HANDLE windowsIdentity) {
      if (!Advapi32.INSTANCE.ImpersonateLoggedOnUser(windowsIdentity)) {
         throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
      }
   }

   public void revertToSelf() {
      Advapi32.INSTANCE.RevertToSelf();
   }
}
