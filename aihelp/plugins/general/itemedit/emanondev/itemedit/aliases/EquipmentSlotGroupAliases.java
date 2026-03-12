package emanondev.itemedit.aliases;

import java.util.Arrays;
import java.util.Collection;
import org.bukkit.inventory.EquipmentSlotGroup;

public class EquipmentSlotGroupAliases extends AliasSet<EquipmentSlotGroup> {
   public EquipmentSlotGroupAliases() {
      super("equip_group");
   }

   public String getName(EquipmentSlotGroup value) {
      return value.toString();
   }

   public Collection<EquipmentSlotGroup> getValues() {
      return Arrays.asList(EquipmentSlotGroup.ANY, EquipmentSlotGroup.ARMOR, EquipmentSlotGroup.CHEST, EquipmentSlotGroup.FEET, EquipmentSlotGroup.HAND, EquipmentSlotGroup.HEAD, EquipmentSlotGroup.LEGS, EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.OFFHAND);
   }
}
