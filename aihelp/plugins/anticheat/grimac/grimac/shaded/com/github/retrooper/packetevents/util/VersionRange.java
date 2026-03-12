package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Arrays;
import java.util.function.Consumer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
public final class VersionRange {
   public static final VersionRange ALL_VERSIONS = new VersionRange((ClientVersion)null);
   private static final ClientVersion[] VERSIONS = ClientVersion.values();
   private final ClientVersion minimum;
   private final ClientVersion maximum;

   public VersionRange(@Nullable ClientVersion version) {
      this(version, version);
   }

   public VersionRange(@Nullable ClientVersion minimum, @Nullable ClientVersion maximum) {
      this.minimum = minimum != null ? minimum : ClientVersion.getOldest();
      this.maximum = maximum != null ? maximum : ClientVersion.getLatest();
      if (this.minimum.compareTo(this.maximum) > 0) {
         throw new IllegalArgumentException("Minimum version is newer than maximum version: " + this.minimum + " > " + this.maximum);
      }
   }

   public void iterate(Consumer<ClientVersion> consumer) {
      if (this.minimum == this.maximum) {
         consumer.accept(this.maximum);
      } else {
         consumer.accept(this.minimum);

         for(int i = this.minimum.ordinal() + 1; i < this.maximum.ordinal(); ++i) {
            consumer.accept(VERSIONS[i]);
         }

         consumer.accept(this.maximum);
      }

   }

   public ClientVersion[] getAll() {
      return (ClientVersion[])Arrays.copyOfRange(VERSIONS, this.minimum.ordinal(), this.maximum.ordinal() + 1);
   }

   public boolean contains(ClientVersion version) {
      return version.compareTo(this.minimum) >= 0 && version.compareTo(this.maximum) <= 0;
   }

   public ClientVersion getMinimum() {
      return this.minimum;
   }

   public ClientVersion getMaximum() {
      return this.maximum;
   }

   public String toString() {
      return "(" + this.minimum + " to " + this.maximum + ")";
   }
}
