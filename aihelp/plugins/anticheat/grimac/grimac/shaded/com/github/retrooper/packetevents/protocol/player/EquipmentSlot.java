package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public enum EquipmentSlot {
   MAIN_HAND(0),
   OFF_HAND(0),
   BOOTS(1),
   LEGGINGS(2),
   CHEST_PLATE(3),
   HELMET(4),
   BODY(0),
   SADDLE(0);

   private static final EquipmentSlot[] VALUES = values();
   private final byte legacyId;

   private EquipmentSlot(int legacyId) {
      this.legacyId = (byte)legacyId;
   }

   public int getId(ServerVersion version) {
      return this.getId(version.toClientVersion());
   }

   public int getId(ClientVersion version) {
      return version.isOlderThan(ClientVersion.V_1_9) ? this.legacyId : this.ordinal();
   }

   @Nullable
   public static EquipmentSlot getById(ClientVersion version, int id) {
      if (version.isOlderThan(ClientVersion.V_1_9)) {
         EquipmentSlot[] var2 = VALUES;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            EquipmentSlot slot = var2[var4];
            if (slot.getId(version) == id) {
               return slot;
            }
         }

         return null;
      } else {
         return VALUES[id];
      }
   }

   @Nullable
   public static EquipmentSlot getById(ServerVersion version, int id) {
      return getById(version.toClientVersion(), id);
   }

   // $FF: synthetic method
   private static EquipmentSlot[] $values() {
      return new EquipmentSlot[]{MAIN_HAND, OFF_HAND, BOOTS, LEGGINGS, CHEST_PLATE, HELMET, BODY, SADDLE};
   }
}
