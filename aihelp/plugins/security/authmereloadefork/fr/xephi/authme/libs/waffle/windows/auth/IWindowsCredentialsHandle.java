package fr.xephi.authme.libs.waffle.windows.auth;

import com.sun.jna.platform.win32.Sspi;

public interface IWindowsCredentialsHandle {
   void initialize();

   void dispose();

   Sspi.CredHandle getHandle();
}
