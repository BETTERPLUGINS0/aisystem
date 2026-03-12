package fr.xephi.authme.libs.waffle.windows.auth.impl;

import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.SspiUtil;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsCredentialsHandle;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsImpersonationContext;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsSecurityContext;

public class WindowsSecurityContextImpl implements IWindowsSecurityContext {
   private String principalName;
   private String securityPackage;
   private SspiUtil.ManagedSecBufferDesc token;
   private Sspi.CtxtHandle ctx;
   private IWindowsCredentialsHandle credentials;
   private boolean continueFlag;

   public IWindowsImpersonationContext impersonate() {
      return new WindowsSecurityContextImpersonationContextImpl(this.ctx);
   }

   public IWindowsIdentity getIdentity() {
      WinNT.HANDLEByReference phContextToken = new WinNT.HANDLEByReference();
      int rc = Secur32.INSTANCE.QuerySecurityContextToken(this.ctx, phContextToken);
      if (0 != rc) {
         throw new Win32Exception(rc);
      } else {
         return new WindowsIdentityImpl(phContextToken.getValue());
      }
   }

   public String getSecurityPackage() {
      return this.securityPackage;
   }

   public byte[] getToken() {
      return this.token != null && this.token.getBuffer(0).getBytes() != null ? (byte[])this.token.getBuffer(0).getBytes().clone() : null;
   }

   public static IWindowsSecurityContext getCurrent(String securityPackage, String targetName) {
      IWindowsCredentialsHandle credentialsHandle = WindowsCredentialsHandleImpl.getCurrent(securityPackage);
      credentialsHandle.initialize();

      WindowsSecurityContextImpl var4;
      try {
         WindowsSecurityContextImpl ctx = new WindowsSecurityContextImpl();
         ctx.setPrincipalName(WindowsAccountImpl.getCurrentUsername());
         ctx.setCredentialsHandle(credentialsHandle);
         ctx.setSecurityPackage(securityPackage);
         ctx.initialize((Sspi.CtxtHandle)null, (Sspi.SecBufferDesc)null, targetName);
         credentialsHandle = null;
         var4 = ctx;
      } finally {
         if (credentialsHandle != null) {
            credentialsHandle.dispose();
         }

      }

      return var4;
   }

   public void initialize(Sspi.CtxtHandle continueCtx, Sspi.SecBufferDesc continueToken, String targetName) {
      IntByReference attr = new IntByReference();
      this.ctx = new Sspi.CtxtHandle();
      int tokenSize = Sspi.MAX_TOKEN_SIZE;

      int rc;
      do {
         this.token = new SspiUtil.ManagedSecBufferDesc(2, tokenSize);
         rc = Secur32.INSTANCE.InitializeSecurityContext(this.credentials.getHandle(), continueCtx, targetName, 2048, 0, 16, continueToken, 0, this.ctx, this.token, attr, (Sspi.TimeStamp)null);
         switch(rc) {
         case -2146893056:
         case -2146893023:
            tokenSize += Sspi.MAX_TOKEN_SIZE;
            break;
         case 0:
            this.continueFlag = false;
            break;
         case 590610:
            this.continueFlag = true;
            break;
         default:
            throw new Win32Exception(rc);
         }
      } while(rc == -2146893056 || rc == -2146893023);

   }

   public void dispose() {
      dispose(this.ctx);
      if (this.credentials != null) {
         this.credentials.dispose();
      }

   }

   public static boolean dispose(Sspi.CtxtHandle ctx) {
      if (ctx != null && !ctx.isNull()) {
         int rc = Secur32.INSTANCE.DeleteSecurityContext(ctx);
         if (0 != rc) {
            throw new Win32Exception(rc);
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public String getPrincipalName() {
      return this.principalName;
   }

   public void setPrincipalName(String value) {
      this.principalName = value;
   }

   public Sspi.CtxtHandle getHandle() {
      return this.ctx;
   }

   public void setCredentialsHandle(IWindowsCredentialsHandle handle) {
      this.credentials = handle;
   }

   public void setToken(byte[] bytes) {
      this.token = new SspiUtil.ManagedSecBufferDesc(2, bytes);
   }

   public void setSecurityPackage(String value) {
      this.securityPackage = value;
   }

   public void setSecurityContext(Sspi.CtxtHandle phNewServerContext) {
      this.ctx = phNewServerContext;
   }

   public boolean isContinue() {
      return this.continueFlag;
   }

   public void setContinue(boolean b) {
      this.continueFlag = b;
   }
}
