package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class DebugBreezeInfo {
   @Nullable
   private final Integer attackTarget;
   @Nullable
   private final Vector3i jumpTarget;

   public DebugBreezeInfo(@Nullable Integer attackTarget, @Nullable Vector3i jumpTarget) {
      this.attackTarget = attackTarget;
      this.jumpTarget = jumpTarget;
   }

   public static DebugBreezeInfo read(PacketWrapper<?> wrapper) {
      Integer attackTarget = (Integer)wrapper.readOptional(PacketWrapper::readVarInt);
      Vector3i jumpTarget = (Vector3i)wrapper.readOptional(PacketWrapper::readBlockPosition);
      return new DebugBreezeInfo(attackTarget, jumpTarget);
   }

   public static void write(PacketWrapper<?> wrapper, DebugBreezeInfo info) {
      wrapper.writeOptional(info.attackTarget, PacketWrapper::writeVarInt);
      wrapper.writeOptional(info.jumpTarget, PacketWrapper::writeBlockPosition);
   }

   @Nullable
   public Integer getAttackTarget() {
      return this.attackTarget;
   }

   @Nullable
   public Vector3i getJumpTarget() {
      return this.jumpTarget;
   }
}
