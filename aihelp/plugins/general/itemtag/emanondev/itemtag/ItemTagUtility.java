package emanondev.itemtag;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.bukkit.inventory.EquipmentSlot;

public class ItemTagUtility {
   private static final EnumSet<EquipmentSlot> playerEquipmentSlots = loadPlayerEquipmentSlot();

   private static EnumSet<EquipmentSlot> loadPlayerEquipmentSlot() {
      EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
      slots.add(EquipmentSlot.HEAD);
      slots.add(EquipmentSlot.CHEST);
      slots.add(EquipmentSlot.LEGS);
      slots.add(EquipmentSlot.FEET);
      slots.add(EquipmentSlot.HAND);

      try {
         slots.add(EquipmentSlot.valueOf("OFF_HAND"));
      } catch (Throwable var2) {
      }

      return slots;
   }

   /** @deprecated */
   @Deprecated
   public static Set<EquipmentSlot> getPlayerEquipmentSlots() {
      return Collections.unmodifiableSet(playerEquipmentSlots);
   }
}
