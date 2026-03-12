package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class DebugBeeInfo {
   @Nullable
   private final Vector3i hivePos;
   @Nullable
   private final Vector3i flowerPos;
   private final int travelTicks;
   private final List<Vector3i> blacklistedHives;

   public DebugBeeInfo(@Nullable Vector3i hivePos, @Nullable Vector3i flowerPos, int travelTicks, List<Vector3i> blacklistedHives) {
      this.hivePos = hivePos;
      this.flowerPos = flowerPos;
      this.travelTicks = travelTicks;
      this.blacklistedHives = blacklistedHives;
   }

   public static DebugBeeInfo read(PacketWrapper<?> wrapper) {
      Vector3i hivePos = (Vector3i)wrapper.readOptional(PacketWrapper::readBlockPosition);
      Vector3i flowerPos = (Vector3i)wrapper.readOptional(PacketWrapper::readBlockPosition);
      int travelTicks = wrapper.readVarInt();
      List<Vector3i> blacklistedHives = wrapper.readList(PacketWrapper::readBlockPosition);
      return new DebugBeeInfo(hivePos, flowerPos, travelTicks, blacklistedHives);
   }

   public static void write(PacketWrapper<?> wrapper, DebugBeeInfo info) {
      wrapper.writeOptional(info.hivePos, PacketWrapper::writeBlockPosition);
      wrapper.writeOptional(info.flowerPos, PacketWrapper::writeBlockPosition);
      wrapper.writeVarInt(info.travelTicks);
      wrapper.writeList(info.blacklistedHives, PacketWrapper::writeBlockPosition);
   }

   @Nullable
   public Vector3i getHivePos() {
      return this.hivePos;
   }

   @Nullable
   public Vector3i getFlowerPos() {
      return this.flowerPos;
   }

   public int getTravelTicks() {
      return this.travelTicks;
   }

   public List<Vector3i> getBlacklistedHives() {
      return this.blacklistedHives;
   }
}
