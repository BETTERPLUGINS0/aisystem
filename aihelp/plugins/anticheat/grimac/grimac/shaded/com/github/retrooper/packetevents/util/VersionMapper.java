package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Arrays;

@ApiStatus.Internal
public class VersionMapper {
   private final ClientVersion[] versions;
   private final ClientVersion[] reversedVersions;

   public VersionMapper(ClientVersion... versions) {
      this.versions = (ClientVersion[])versions.clone();
      Arrays.sort(this.versions);
      this.reversedVersions = new ClientVersion[this.versions.length];
      int i = this.versions.length - 1;

      for(int j = 0; i >= 0; ++j) {
         this.reversedVersions[j] = this.versions[i];
         --i;
      }

   }

   public VersionMapper withExtra(ClientVersion extraStep) {
      if (Arrays.binarySearch(this.versions, extraStep) >= 0) {
         return this;
      } else {
         ClientVersion[] clonedVersions = (ClientVersion[])Arrays.copyOf(this.versions, this.versions.length + 1);
         clonedVersions[clonedVersions.length - 1] = extraStep;
         return new VersionMapper(clonedVersions);
      }
   }

   public ClientVersion[] getVersions() {
      return this.versions;
   }

   public ClientVersion[] getReversedVersions() {
      return this.reversedVersions;
   }

   public int getIndex(ClientVersion version) {
      int index = this.reversedVersions.length - 1;
      ClientVersion[] var3 = this.reversedVersions;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ClientVersion v = var3[var5];
         if (version.isNewerThanOrEquals(v)) {
            return index;
         }

         --index;
      }

      return 0;
   }

   public int size() {
      return this.versions.length;
   }
}
