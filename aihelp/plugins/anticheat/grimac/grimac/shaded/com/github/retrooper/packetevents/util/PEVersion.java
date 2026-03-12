package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;

public class PEVersion implements Comparable<PEVersion> {
   private final int major;
   private final int minor;
   private final int patch;
   private final boolean snapshot;
   @Nullable
   private final String snapshotCommit;

   public PEVersion(final int major, final int minor, final int patch, final boolean snapshot, @Nullable final String snapshotCommit) {
      this.major = major;
      this.minor = minor;
      this.patch = patch;
      this.snapshot = snapshot;
      this.snapshotCommit = snapshotCommit;
   }

   public PEVersion(final int major, final int minor, final int patch, @Nullable final String snapshotCommit) {
      this(major, minor, patch, snapshotCommit != null, snapshotCommit);
   }

   public PEVersion(final int major, final int minor, final int patch, final boolean snapshot) {
      this(major, minor, patch, snapshot, (String)null);
   }

   public PEVersion(final int major, final int minor, final int patch) {
      this(major, minor, patch, false);
   }

   /** @deprecated */
   @Deprecated
   public PEVersion(final int... version) {
      this(version[0], version[1], version[2]);
   }

   /** @deprecated */
   @Deprecated
   public PEVersion(@NotNull final String version) {
      String versionWithoutSnapshot = version.replace("-SNAPSHOT", "");
      String[] largeParts = versionWithoutSnapshot.split("\\+");
      String[] parts = largeParts.length > 0 ? largeParts[0].split("\\.") : null;
      if (largeParts.length >= 1 && largeParts.length <= 2 && parts.length >= 2 && parts.length <= 3) {
         this.major = Integer.parseInt(parts[0]);
         this.minor = Integer.parseInt(parts[1]);
         this.patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
         this.snapshot = version.contains("-SNAPSHOT");
         this.snapshotCommit = largeParts.length > 1 ? largeParts[1] : null;
      } else {
         throw new IllegalArgumentException("Version string must be in the format 'major.minor[.patch][+commit][-SNAPSHOT]', found '" + version + "' instead");
      }
   }

   public static PEVersion fromString(@NotNull final String version) {
      return new PEVersion(version);
   }

   public int major() {
      return this.major;
   }

   public int minor() {
      return this.minor;
   }

   public int patch() {
      return this.patch;
   }

   public boolean snapshot() {
      return this.snapshot;
   }

   @Nullable
   public String snapshotCommit() {
      return this.snapshotCommit;
   }

   public int compareTo(@NotNull final PEVersion other) {
      int majorCompare = Integer.compare(this.major, other.major);
      if (majorCompare != 0) {
         return majorCompare;
      } else {
         int minorCompare = Integer.compare(this.minor, other.minor);
         if (minorCompare != 0) {
            return minorCompare;
         } else {
            int patchCompare = Integer.compare(this.patch, other.patch);
            return patchCompare != 0 ? patchCompare : Boolean.compare(other.snapshot, this.snapshot);
         }
      }
   }

   public boolean equals(@NotNull final Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof PEVersion)) {
         return false;
      } else {
         PEVersion other = (PEVersion)obj;
         return this.major == other.major && this.minor == other.minor && this.patch == other.patch && this.snapshot == other.snapshot && Objects.equals(this.snapshotCommit, other.snapshotCommit);
      }
   }

   public boolean isNewerThan(@NotNull final PEVersion otherVersion) {
      return this.compareTo(otherVersion) > 0;
   }

   public boolean isOlderThan(@NotNull final PEVersion otherVersion) {
      return this.compareTo(otherVersion) < 0;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.major, this.minor, this.patch, this.snapshot, this.snapshotCommit});
   }

   public PEVersion clone() {
      return new PEVersion(this.major, this.minor, this.patch, this.snapshot, this.snapshotCommit);
   }

   public String toString() {
      return this.major + "." + this.minor + "." + this.patch + (this.snapshot && this.snapshotCommit != null ? "+" + this.snapshotCommit + "-SNAPSHOT" : "");
   }

   public String toStringWithoutSnapshot() {
      return this.major + "." + this.minor + "." + this.patch;
   }

   /** @deprecated */
   @Deprecated
   public int[] asArray() {
      return new int[]{this.major, this.minor, this.patch};
   }
}
