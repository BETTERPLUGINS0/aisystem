package fr.xephi.authme.libs.waffle.windows.auth.impl;

import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.Win32Exception;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsImpersonationContext;

public class WindowsSecurityContextImpersonationContextImpl implements IWindowsImpersonationContext {
   private final Sspi.CtxtHandle ctx;

   public WindowsSecurityContextImpersonationContextImpl(Sspi.CtxtHandle newCtx) {
      int rc = Secur32.INSTANCE.ImpersonateSecurityContext(newCtx);
      if (rc != 0) {
         throw new Win32Exception(rc);
      } else {
         this.ctx = newCtx;
      }
   }

   public void revertToSelf() {
      int rc = Secur32.INSTANCE.RevertSecurityContext(this.ctx);
      if (rc != 0) {
         throw new Win32Exception(rc);
      }
   }
}
