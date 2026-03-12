package com.nisovin.shopkeepers.ui.equipmentEditor;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.bukkit.inventory.EquipmentSlot;

public class EquipmentEditorUIState implements UIState {
   private final List<? extends EquipmentSlot> supportedSlots;
   private final Map<? extends EquipmentSlot, ? extends UnmodifiableItemStack> currentEquipment;
   private final BiConsumer<EquipmentSlot, UnmodifiableItemStack> onEquipmentChanged;

   public EquipmentEditorUIState(List<? extends EquipmentSlot> supportedSlots, Map<? extends EquipmentSlot, ? extends UnmodifiableItemStack> currentEquipment, BiConsumer<EquipmentSlot, UnmodifiableItemStack> onEquipmentChanged) {
      Validate.notNull(supportedSlots, (String)"supportedSlots is null");
      Validate.notNull(currentEquipment, (String)"currentEquipment is null");
      Validate.notNull(onEquipmentChanged, (String)"onEquipmentChanged is null");
      this.supportedSlots = supportedSlots;
      this.currentEquipment = currentEquipment;
      this.onEquipmentChanged = onEquipmentChanged;
   }

   public List<? extends EquipmentSlot> getSupportedSlots() {
      return this.supportedSlots;
   }

   public Map<? extends EquipmentSlot, ? extends UnmodifiableItemStack> getCurrentEquipment() {
      return this.currentEquipment;
   }

   public BiConsumer<EquipmentSlot, UnmodifiableItemStack> getOnEquipmentChanged() {
      return this.onEquipmentChanged;
   }
}
