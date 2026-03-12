package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.updater;

public final class Version {
   private final VersionNumber versionNumber;
   private final String downloadUrl;
   private final String changelogUrl;
   private final UpdateChannel channel;
   private final VersionNumber minMinecraftVersion;
   private final VersionNumber maxMinecraftVersion;

   Version(VersionNumber versionNumber, String downloadUrl, String changelogUrl, UpdateChannel channel, VersionNumber minMinecraftVersion, VersionNumber maxMinecraftVersion) {
      this.versionNumber = versionNumber;
      this.downloadUrl = downloadUrl;
      this.changelogUrl = changelogUrl;
      this.channel = channel;
      this.minMinecraftVersion = minMinecraftVersion;
      this.maxMinecraftVersion = maxMinecraftVersion;
   }

   public static Version.VersionBuilder builder() {
      return new Version.VersionBuilder();
   }

   public VersionNumber getVersionNumber() {
      return this.versionNumber;
   }

   public String getDownloadUrl() {
      return this.downloadUrl;
   }

   public String getChangelogUrl() {
      return this.changelogUrl;
   }

   public UpdateChannel getChannel() {
      return this.channel;
   }

   public VersionNumber getMinMinecraftVersion() {
      return this.minMinecraftVersion;
   }

   public VersionNumber getMaxMinecraftVersion() {
      return this.maxMinecraftVersion;
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Version)) {
         return false;
      } else {
         Version other = (Version)o;
         Object this$versionNumber = this.getVersionNumber();
         Object other$versionNumber = other.getVersionNumber();
         if (this$versionNumber == null) {
            if (other$versionNumber != null) {
               return false;
            }
         } else if (!this$versionNumber.equals(other$versionNumber)) {
            return false;
         }

         label73: {
            Object this$downloadUrl = this.getDownloadUrl();
            Object other$downloadUrl = other.getDownloadUrl();
            if (this$downloadUrl == null) {
               if (other$downloadUrl == null) {
                  break label73;
               }
            } else if (this$downloadUrl.equals(other$downloadUrl)) {
               break label73;
            }

            return false;
         }

         Object this$changelogUrl = this.getChangelogUrl();
         Object other$changelogUrl = other.getChangelogUrl();
         if (this$changelogUrl == null) {
            if (other$changelogUrl != null) {
               return false;
            }
         } else if (!this$changelogUrl.equals(other$changelogUrl)) {
            return false;
         }

         label59: {
            Object this$channel = this.getChannel();
            Object other$channel = other.getChannel();
            if (this$channel == null) {
               if (other$channel == null) {
                  break label59;
               }
            } else if (this$channel.equals(other$channel)) {
               break label59;
            }

            return false;
         }

         Object this$minMinecraftVersion = this.getMinMinecraftVersion();
         Object other$minMinecraftVersion = other.getMinMinecraftVersion();
         if (this$minMinecraftVersion == null) {
            if (other$minMinecraftVersion != null) {
               return false;
            }
         } else if (!this$minMinecraftVersion.equals(other$minMinecraftVersion)) {
            return false;
         }

         Object this$maxMinecraftVersion = this.getMaxMinecraftVersion();
         Object other$maxMinecraftVersion = other.getMaxMinecraftVersion();
         if (this$maxMinecraftVersion == null) {
            if (other$maxMinecraftVersion != null) {
               return false;
            }
         } else if (!this$maxMinecraftVersion.equals(other$maxMinecraftVersion)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      Object $versionNumber = this.getVersionNumber();
      int result = result * 59 + ($versionNumber == null ? 43 : $versionNumber.hashCode());
      Object $downloadUrl = this.getDownloadUrl();
      result = result * 59 + ($downloadUrl == null ? 43 : $downloadUrl.hashCode());
      Object $changelogUrl = this.getChangelogUrl();
      result = result * 59 + ($changelogUrl == null ? 43 : $changelogUrl.hashCode());
      Object $channel = this.getChannel();
      result = result * 59 + ($channel == null ? 43 : $channel.hashCode());
      Object $minMinecraftVersion = this.getMinMinecraftVersion();
      result = result * 59 + ($minMinecraftVersion == null ? 43 : $minMinecraftVersion.hashCode());
      Object $maxMinecraftVersion = this.getMaxMinecraftVersion();
      result = result * 59 + ($maxMinecraftVersion == null ? 43 : $maxMinecraftVersion.hashCode());
      return result;
   }

   public String toString() {
      return "Version(versionNumber=" + this.getVersionNumber() + ", downloadUrl=" + this.getDownloadUrl() + ", changelogUrl=" + this.getChangelogUrl() + ", channel=" + this.getChannel() + ", minMinecraftVersion=" + this.getMinMinecraftVersion() + ", maxMinecraftVersion=" + this.getMaxMinecraftVersion() + ")";
   }

   public static class VersionBuilder {
      private VersionNumber versionNumber;
      private String downloadUrl;
      private String changelogUrl;
      private UpdateChannel channel;
      private VersionNumber minMinecraftVersion;
      private VersionNumber maxMinecraftVersion;

      VersionBuilder() {
      }

      public Version.VersionBuilder versionNumber(VersionNumber versionNumber) {
         this.versionNumber = versionNumber;
         return this;
      }

      public Version.VersionBuilder downloadUrl(String downloadUrl) {
         this.downloadUrl = downloadUrl;
         return this;
      }

      public Version.VersionBuilder changelogUrl(String changelogUrl) {
         this.changelogUrl = changelogUrl;
         return this;
      }

      public Version.VersionBuilder channel(UpdateChannel channel) {
         this.channel = channel;
         return this;
      }

      public Version.VersionBuilder minMinecraftVersion(VersionNumber minMinecraftVersion) {
         this.minMinecraftVersion = minMinecraftVersion;
         return this;
      }

      public Version.VersionBuilder maxMinecraftVersion(VersionNumber maxMinecraftVersion) {
         this.maxMinecraftVersion = maxMinecraftVersion;
         return this;
      }

      public Version build() {
         return new Version(this.versionNumber, this.downloadUrl, this.changelogUrl, this.channel, this.minMinecraftVersion, this.maxMinecraftVersion);
      }

      public String toString() {
         return "Version.VersionBuilder(versionNumber=" + this.versionNumber + ", downloadUrl=" + this.downloadUrl + ", changelogUrl=" + this.changelogUrl + ", channel=" + this.channel + ", minMinecraftVersion=" + this.minMinecraftVersion + ", maxMinecraftVersion=" + this.maxMinecraftVersion + ")";
      }
   }
}
