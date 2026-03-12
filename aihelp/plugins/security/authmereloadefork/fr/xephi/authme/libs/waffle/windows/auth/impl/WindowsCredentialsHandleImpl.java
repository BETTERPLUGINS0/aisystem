package fr.xephi.authme.libs.waffle.windows.auth.impl;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsCredentialsHandle;

public class WindowsCredentialsHandleImpl implements IWindowsCredentialsHandle {
   private final String principalName;
   private final int credentialsType;
   private final String securityPackage;
   private Sspi.CredHandle handle;

   public WindowsCredentialsHandleImpl(String newPrincipalName, int newCredentialsType, String newSecurityPackage) {
      this.principalName = newPrincipalName;
      this.credentialsType = newCredentialsType;
      this.securityPackage = newSecurityPackage;
   }

   public static IWindowsCredentialsHandle getCurrent(String securityPackage) {
      IWindowsCredentialsHandle handle = new WindowsCredentialsHandleImpl((String)null, 2, securityPackage);
      handle.initialize();
      return handle;
   }

   public void initialize() {
      this.handle = new Sspi.CredHandle();
      Sspi.TimeStamp clientLifetime = new Sspi.TimeStamp();
      int rc = Secur32.INSTANCE.AcquireCredentialsHandle(this.principalName, this.securityPackage, this.credentialsType, (WinNT.LUID)null, (Pointer)null, (Pointer)null, (Pointer)null, this.handle, clientLifetime);
      if (0 != rc) {
         throw new Win32Exception(rc);
      }
   }

   public void dispose() {
      if (this.handle != null && !this.handle.isNull()) {
         int rc = Secur32.INSTANCE.FreeCredentialsHandle(this.handle);
         if (0 != rc) {
            throw new Win32Exception(rc);
         }
      }

   }

   public Sspi.CredHandle getHandle() {
      return this.handle;
   }
}
