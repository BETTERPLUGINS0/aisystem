package fr.xephi.authme.libs.waffle.windows.auth;

public interface IWindowsAccount {
   String getSidString();

   String getFqn();

   String getName();

   String getDomain();
}
