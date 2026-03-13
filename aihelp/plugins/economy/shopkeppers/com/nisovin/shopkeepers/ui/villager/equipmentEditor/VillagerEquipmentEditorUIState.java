package com.nisovin.shopkeepers.ui.villager.equipmentEditor;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.ui.equipmentEditor.EquipmentEditorUIState;
import com.nisovin.shopkeepers.util.bukkit.EquipmentUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class VillagerEquipmentEditorUIState extends EquipmentEditorUIState {
   private static Map<? extends EquipmentSlot, ? extends UnmodifiableItemStack> getEquipmentMap(LivingEntity entity) {
      Validate.notNull(entity, (String)"entity is null");
      EntityEquipment equipment = entity.getEquipment();
      if (equipment == null) {
         return Collections.emptyMap();
      } else {
         Map<EquipmentSlot, UnmodifiableItemStack> equipmentMap = new EnumMap(EquipmentSlot.class);
         Iterator var3 = EquipmentUtils.EQUIPMENT_SLOTS.iterator();

         while(var3.hasNext()) {
            EquipmentSlot slot = (EquipmentSlot)var3.next();
            ItemStack item = equipment.getItem(slot);
            if (!ItemUtils.isEmpty(item)) {
               assert item != null;

               equipmentMap.put(slot, UnmodifiableItemStack.ofNonNull(item));
            }
         }

         return equipmentMap;
      }
   }

   public VillagerEquipmentEditorUIState(AbstractVillager villager) {
      super(EquipmentUtils.EQUIPMENT_SLOTS, getEquipmentMap(villager), (slot, item) -> {
         assert villager.isValid();

         EntityEquipment entityEquipment = (EntityEquipment)Unsafe.assertNonNull(villager.getEquipment());
         entityEquipment.setItem(slot, ItemUtils.asItemStackOrNull(item));
      });
   }
}
