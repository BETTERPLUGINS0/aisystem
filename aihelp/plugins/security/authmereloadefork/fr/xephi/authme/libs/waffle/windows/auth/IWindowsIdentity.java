package fr.xephi.authme.libs.waffle.windows.auth;

public interface IWindowsIdentity {
   String getSidString();

   byte[] getSid();

   String getFqn();

   IWindowsAccount[] getGroups();

   IWindowsImpersonationContext impersonate();

   void dispose();

   boolean isGuest();
}
