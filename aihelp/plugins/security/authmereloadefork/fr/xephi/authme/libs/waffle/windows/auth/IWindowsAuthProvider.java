package fr.xephi.authme.libs.waffle.windows.auth;

public interface IWindowsAuthProvider {
   IWindowsIdentity logonUser(String var1, String var2);

   IWindowsIdentity logonDomainUser(String var1, String var2, String var3);

   IWindowsIdentity logonDomainUserEx(String var1, String var2, String var3, int var4, int var5);

   IWindowsAccount lookupAccount(String var1);

   IWindowsComputer getCurrentComputer();

   IWindowsDomain[] getDomains();

   IWindowsSecurityContext acceptSecurityToken(String var1, byte[] var2, String var3);

   void resetSecurityToken(String var1);
}
