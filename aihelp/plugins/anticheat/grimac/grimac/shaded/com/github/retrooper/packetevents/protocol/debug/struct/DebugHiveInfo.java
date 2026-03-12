package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.struct;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DebugHiveInfo {
   private final StateType type;
   private final int occupantCount;
   private final int honeyLevel;
   private final boolean sedated;

   public DebugHiveInfo(StateType type, int occupantCount, int honeyLevel, boolean sedated) {
      this.type = type;
      this.occupantCount = occupantCount;
      this.honeyLevel = honeyLevel;
      this.sedated = sedated;
   }

   public static DebugHiveInfo read(PacketWrapper<?> wrapper) {
      StateType type = ((StateType.Mapped)wrapper.readMappedEntity((IRegistry)StateTypes.getRegistry())).getStateType();
      int occupantCount = wrapper.readVarInt();
      int honeyLevel = wrapper.readVarInt();
      boolean sedated = wrapper.readBoolean();
      return new DebugHiveInfo(type, occupantCount, honeyLevel, sedated);
   }

   public static void write(PacketWrapper<?> wrapper, DebugHiveInfo info) {
      wrapper.writeMappedEntity(info.type.getMapped());
      wrapper.writeVarInt(info.occupantCount);
      wrapper.writeVarInt(info.honeyLevel);
      wrapper.writeBoolean(info.sedated);
   }

   public StateType getType() {
      return this.type;
   }

   public int getOccupantCount() {
      return this.occupantCount;
   }

   public int getHoneyLevel() {
      return this.honeyLevel;
   }

   public boolean isSedated() {
      return this.sedated;
   }
}
