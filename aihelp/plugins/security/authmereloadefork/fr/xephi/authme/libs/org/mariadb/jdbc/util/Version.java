package fr.xephi.authme.libs.org.mariadb.jdbc.util;

public class Version {
   private final String version;
   private final int majorVersion;
   private final int minorVersion;
   private final int patchVersion;
   private final String qualifier;

   public Version(String versionString) {
      this.version = versionString;
      int major = 0;
      int minor = 0;
      int patch = 0;
      String qualif = "";
      int length = this.version.length();
      int offset = 0;
      int type = 0;

      int val;
      for(val = 0; offset < length; ++offset) {
         char car = this.version.charAt(offset);
         if (car >= '0' && car <= '9') {
            val = val * 10 + car - 48;
         } else {
            switch(type) {
            case 0:
               major = val;
               break;
            case 1:
               minor = val;
               break;
            case 2:
               patch = val;
               qualif = this.version.substring(offset);
               offset = length;
            }

            ++type;
            val = 0;
         }
      }

      if (type == 2) {
         patch = val;
      }

      this.majorVersion = major;
      this.minorVersion = minor;
      this.patchVersion = patch;
      this.qualifier = qualif;
   }

   public String getVersion() {
      return this.version;
   }

   public int getMajorVersion() {
      return this.majorVersion;
   }

   public int getMinorVersion() {
      return this.minorVersion;
   }

   public int getPatchVersion() {
      return this.patchVersion;
   }

   public String getQualifier() {
      return this.qualifier;
   }

   public boolean versionFixedMajorMinorGreaterOrEqual(int major, int minor, int patch) {
      return this.majorVersion == major && this.minorVersion == minor && this.patchVersion >= patch;
   }

   public boolean versionGreaterOrEqual(int major, int minor, int patch) {
      if (this.majorVersion > major) {
         return true;
      } else if (this.majorVersion < major) {
         return false;
      } else if (this.minorVersion > minor) {
         return true;
      } else if (this.minorVersion < minor) {
         return false;
      } else {
         return this.patchVersion >= patch;
      }
   }
}
