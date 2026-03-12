package fr.xephi.authme.libs.waffle.windows.auth;

public interface IWindowsComputer {
   String getComputerName();

   String getMemberOf();

   String getJoinStatus();

   String[] getGroups();
}
