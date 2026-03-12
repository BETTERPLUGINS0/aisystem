package fr.xephi.authme.libs.waffle.windows.auth;

import com.sun.jna.platform.win32.Sspi;

public interface IWindowsSecurityContext {
   String getSecurityPackage();

   String getPrincipalName();

   byte[] getToken();

   boolean isContinue();

   IWindowsIdentity getIdentity();

   Sspi.CtxtHandle getHandle();

   void initialize(Sspi.CtxtHandle var1, Sspi.SecBufferDesc var2, String var3);

   IWindowsImpersonationContext impersonate();

   void dispose();
}
