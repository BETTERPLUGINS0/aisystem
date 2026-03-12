package fr.xephi.authme.libs.waffle.windows.auth.impl;

import com.sun.jna.platform.win32.Netapi32Util;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsComputer;
import java.util.ArrayList;
import java.util.List;

public class WindowsComputerImpl implements IWindowsComputer {
   private final String computerName;
   private final String domainName;

   public WindowsComputerImpl(String newComputerName) {
      this.computerName = newComputerName;
      this.domainName = Netapi32Util.getDomainName(newComputerName);
   }

   public String getComputerName() {
      return this.computerName;
   }

   public String[] getGroups() {
      List<String> groupNames = new ArrayList();
      Netapi32Util.LocalGroup[] groups = Netapi32Util.getLocalGroups(this.computerName);
      Netapi32Util.LocalGroup[] var3 = groups;
      int var4 = groups.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Netapi32Util.LocalGroup group = var3[var5];
         groupNames.add(group.name);
      }

      return (String[])groupNames.toArray(new String[0]);
   }

   public String getJoinStatus() {
      int joinStatus = Netapi32Util.getJoinStatus(this.computerName);
      switch(joinStatus) {
      case 0:
         return "NetSetupUnknownStatus";
      case 1:
         return "NetSetupUnjoined";
      case 2:
         return "NetSetupWorkgroupName";
      case 3:
         return "NetSetupDomainName";
      default:
         throw new RuntimeException("Unsupported join status: " + joinStatus);
      }
   }

   public String getMemberOf() {
      return this.domainName;
   }
}
