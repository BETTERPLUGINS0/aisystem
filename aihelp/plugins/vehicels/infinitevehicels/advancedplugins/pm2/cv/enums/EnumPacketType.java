package advancedplugins.pm2.cv.enums;

import advancedplugins.pm2.cv.api.enums.MinecraftVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum EnumPacketType {
   INCOMING_INTERACT("PacketPlayInUseEntity", "ServerboundInteractPacket"),
   INCOMING_SWING_ARM("PacketPlayInArmAnimation", "ServerboundSwingPacket"),
   INCOMING_PLAYER_INPUT("PacketPlayInSteerVehicle", "ServerboundPlayerInputPacket"),
   INCOMING_PLAYER_ACTION("PacketPlayInBlockDig", "ServerboundPlayerActionPacket"),
   INCOMING_USE_ITEM("PacketPlayInBlockPlace", "ServerboundUseItemPacket"),
   INCOMING_ENTITY_ACTION("PacketPlayInEntityAction", "ServerboundPlayerCommandPacket"),
   INCOMING_LOOK("PacketPlayInFlying/PacketPlayInLook", "ServerboundMovePlayerPacket$Rot"),
   INCOMING_POSITION("PacketPlayInFlying/PacketPlayInPosition", "ServerboundMovePlayerPacket$Pos"),
   INCOMING_POSITION_LOOK("PacketPlayInFlying/PacketPlayInPositionLook", "ServerboundMovePlayerPacket$PosRot"),
   OUTGOING_TELEPORT_ENTITY("PacketPlayOutEntityTeleport", "ClientboundTeleportEntityPacket"),
   OUTGOING_UPDATE_ENTITY_POSITION("PacketPlayOutEntity/PacketPlayOutRelEntityMove", "ClientboundMoveEntityPacket$Pos"),
   OUTGOING_UPDATE_ENTITY_ROTATION("PacketPlayOutEntity/PacketPlayOutEntityLook", "ClientboundMoveEntityPacket$Rot"),
   OUTGOING_UPDATE_ENTITY_POSITION_ROTATION("PacketPlayOutEntity/PacketPlayOutRelEntityMoveLook", "ClientboundMoveEntityPacket$PosRot");

   private static final String PROTOCOL_PACKAGE = "net.minecraft.network.protocol.game.";
   @NotNull
   private final Class<?> packetClass;

   @Nullable
   public static EnumPacketType of(@NotNull Object packet) {
      EnumPacketType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumPacketType var4 = var1[var3];
         if (var0.getClass() == var4.packetClass) {
            return var4;
         }
      }

      return null;
   }

   private EnumPacketType(String className) {
      this(var3, (String)null);
   }

   private EnumPacketType(String className, String paperClassName) {
      try {
         String var5 = MinecraftVersion.isPaper() && MinecraftVersion.isNewerThan(MinecraftVersion.MC1_20_R4) && var4 != null ? var4 : var3;
         if (var5.contains("/")) {
            String[] var6 = var5.split("/");
            Class var7 = Class.forName("net.minecraft.network.protocol.game." + var6[0]);
            Class var8 = null;
            Class[] var9 = var7.getDeclaredClasses();
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               Class var12 = var9[var11];
               if (var12.getSimpleName().equals(var6[1])) {
                  var8 = var12;
                  break;
               }
            }

            if (var8 == null) {
               throw new IllegalStateException("Unsupported server version: " + var5);
            }

            this.packetClass = var8;
         } else {
            this.packetClass = Class.forName("net.minecraft.network.protocol.game." + var5);
         }

      } catch (ClassNotFoundException var13) {
         throw new IllegalStateException("Unsupported server version: " + (MinecraftVersion.isPaper() ? var4 : var3), var13);
      }
   }

   @NotNull
   public Class<?> getPacketClass() {
      return this.packetClass;
   }

   public boolean is(@NotNull Object packet) {
      return var1.getClass() == this.packetClass;
   }

   // $FF: synthetic method
   private static EnumPacketType[] $values() {
      return new EnumPacketType[]{INCOMING_INTERACT, INCOMING_SWING_ARM, INCOMING_PLAYER_INPUT, INCOMING_PLAYER_ACTION, INCOMING_USE_ITEM, INCOMING_ENTITY_ACTION, INCOMING_LOOK, INCOMING_POSITION, INCOMING_POSITION_LOOK, OUTGOING_TELEPORT_ENTITY, OUTGOING_UPDATE_ENTITY_POSITION, OUTGOING_UPDATE_ENTITY_ROTATION, OUTGOING_UPDATE_ENTITY_POSITION_ROTATION};
   }
}
