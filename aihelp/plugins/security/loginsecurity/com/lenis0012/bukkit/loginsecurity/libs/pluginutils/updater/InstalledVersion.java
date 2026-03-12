package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater;

public final class InstalledVersion {
   private final Version version;

   public InstalledVersion(Version version) {
      this.version = version;
   }

   public Version getVersion() {
      return this.version;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof InstalledVersion)) {
         return false;
      } else {
         InstalledVersion other = (InstalledVersion)o;
         Object this$version = this.getVersion();
         Object other$version = other.getVersion();
         if (this$version == null) {
            if (other$version != null) {
               return false;
            }
         } else if (!this$version.equals(other$version)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $version = this.getVersion();
      int result = result * 59 + ($version == null ? 43 : $version.hashCode());
      return result;
   }

   public String toString() {
      return "InstalledVersion(version=" + this.getVersion() + ")";
   }
}
